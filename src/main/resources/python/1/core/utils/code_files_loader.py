import os
from .code_file import CodeFile


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
        if file_path.endswith(".java"):
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
