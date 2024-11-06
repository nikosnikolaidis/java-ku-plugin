import os
from datetime import datetime
from config.settings import settings_instance
from core.utils.code_preprocessing import remove_comments, remove_packages, remove_imports


def get_contributions_from_diffs(commit, diffs):
    contributions = []

    for diff in diffs:
        # Determine the relevant file path
        relevant_path = diff.b_path if diff.b_path else diff.a_path

        if relevant_path.endswith(f".{settings_instance.FILE_TYPE}"):
            try:
                # Extract the content of the file for the specific commit
                file_content = commit.tree / relevant_path
                filename = os.path.basename(relevant_path)
                base, ext = os.path.splitext(filename)

                # Convert the relevant path to a format suitable for a filename
                sanitized_path = relevant_path.replace("/", "_")
                sanitized_path = sanitized_path.rsplit('.', 1)[0]
                sanitized_path = sanitized_path.replace(".", "-")
                sanitized_filename = f"{sanitized_path}_{commit.hexsha[:7]}{ext}"

                temp_filepath = os.path.join(settings_instance.TEMP_FILES_BASE_PATH, sanitized_filename)

                content = file_content.data_stream.read()
                with open(temp_filepath, "wb") as temp_file:
                    temp_file.write(content)
                content = content.decode("utf-8")
            except KeyError:
                # If the file was deleted or renamed, skip it
                continue

            if commit.parents:
                diff_lines = diff.diff.decode("utf-8").splitlines()

                # Extract line numbers from the diff's hunks
                line_numbers = []
                current_line_num = None

                for line in diff_lines:
                    if line.startswith("@@"):
                        # This is a hunk header; format: @@ -start_line,num_lines +start_line,num_lines @@
                        current_line_num = (
                                int(line.split(" ")[2].split(",")[0][1:]) - 1
                        )
                    elif line.startswith("+"):
                        if not line.startswith("+++") and line_is_accepted(line, content):
                            line_numbers.append(current_line_num)
                    if current_line_num is not None:
                        if not line.startswith("-"):
                            current_line_num += 1
            else:
                # For the first commit, consider all lines as added
                line_numbers = list(range(1, len(content.splitlines()) + 1))

            if line_numbers:
                contributions.append(
                    {
                        "author": commit.author.name,  # Use email if multiple authors have the same name
                        "file_content": content,
                        "changed_lines": line_numbers,
                        "temp_filepath": temp_filepath,
                        "timestamp": datetime.fromtimestamp(
                            commit.committed_date
                        ).isoformat(),
                        "sha": commit.hexsha,
                    }
                )

    return contributions


def line_is_accepted(line, content):
    if line[1:].strip() in ["", "*", "//"]:
        return False

    # If line doesn't exist after the comments were removed, it was a comment
    content = remove_comments(content)
    content = remove_packages(content)
    content = remove_imports(content)
    if line[1:] not in content:
        return False
    return True
