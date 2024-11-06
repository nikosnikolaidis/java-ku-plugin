import os
from .code_file import CodeFile

from .code_file import CodeFile
import os

def read_all_files_in_repo(repo_path: str):
    """
    Διαβάζει όλα τα αρχεία μέσα σε ένα repository και επιστρέφει το περιεχόμενό τους.

    Parameters:
        repo_path (str): Το μονοπάτι προς το repository.

    Returns:
        dict: Ένα λεξικό με τα filenames ως κλειδιά και το περιεχόμενο των αρχείων ως τιμές.
    """
    contents = {}

    # Διασχίζουμε όλους τους υποκαταλόγους και αρχεία μέσα στο repo_path
    for root, dirs, files in os.walk(repo_path):
        for file in files:
            if file.endswith(".java"):
                file_path = os.path.join(root, file)
                filename = os.path.basename(file_path)  # Πάρε μόνο το όνομα αρχείου από το μονοπάτι

                # Διαβάζουμε το περιεχόμενο του αρχείου
                with open(file_path, 'r', encoding='utf-8') as f:
                    file_content = f.read()

                contents[file_path] = CodeFile(filename, file_content, "",
                                              "", "")
                # Προσθέτουμε το αρχείο στο λεξικό με το πλήρες όνομα ως κλειδί και το περιεχόμενο ως τιμή

    return contents


def read_files_from_directory(directory: str):
    """
    Read the contents of all .java files in the specified directory. The author and timestamp fields are left empty.

    Parameters:
        directory (str): The directory containing the .java files.

    Returns:
        dict: A dictionary with filenames as keys and their CodeFile objects as values.
    """
    files = [f for f in os.listdir(directory) if f.endswith(".java")]
    contents = {}

    for filename in files:
        file_path = os.path.join(directory, filename)
        with open(file_path, "r", encoding="utf-8") as f:
            contents[filename] = CodeFile(filename, f.read())

    return contents


def read_files_from_commit(commit):
    files = {}

    # Βρες τα αρχεία και το περιεχόμενό τους
    for file_path in commit.stats.files.keys():
        filename = os.path.basename(file_path)  # Πάρε μόνο το όνομα αρχείου από το μονοπάτι
        try:
            # Ανάκτηση του περιεχομένου του αρχείου από το commit
            blob = commit.tree[file_path]
            file_content = blob.data_stream.read().decode('utf-8')
        except KeyError:
            file_content = "File not found in the tree."

        # Συγκέντρωση των πληροφοριών
        code_file = CodeFile(
            filename=filename,
            content=file_content,
            author=commit.author.name,
            timestamp=commit.committed_datetime.isoformat(),
            sha=commit.hexsha
        )

        files[file_path] = code_file

    return files


def read_files_from_dict_list(dict_list: list):
    """
    Read the contents of all .java files in the directories found inside the git contribution dictionaries.

    Parameters:
        dict_list (list): A list of git contribution dictionaries.

    Returns:
        dict: A dictionary with filenames as keys and their CodeFile objects as values.
    """
    contents = {}

    for contribution in dict_list:
        filename = os.path.basename(contribution["temp_filepath"]).split(".")[0]
        contents[filename] = CodeFile(filename, contribution["file_content"], author=contribution["author"],
                                      timestamp=contribution["timestamp"], sha=contribution["sha"])

    return contents
