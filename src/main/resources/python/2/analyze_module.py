import os
import git
import time
import json
from core.git_operations import clone_repo, repo_exists
from core.utils.code_files_loader import read_files_from_commit, read_all_files_in_repo
from core.analysis.codebert_sliding_window import codebert_sliding_window
from config.settings import settings_instance
from core.ml_operations.loader import load_codebert_model
def analyze(repo_path):
    try:
        # Φορτώνουμε το μοντέλο CodeBERT κατά την αρχικοποίηση
        model = load_codebert_model(settings_instance.CODEBERT_BASE_PATH, 27)

        # Εξαγωγή του ονόματος του αποθετηρίου από το URL
        repo_name = os.path.basename(repo_path)
        #print(f"Analyzing repo: {repo_url}, SHA: {sha}")

        # Έλεγχος ότι το repo_url  έχουν δοθεί
        if not repo_path:
            raise ValueError("Repo_url is required")

        files = read_all_files_in_repo(repo_path)


        if not files:
            raise FileNotFoundError(f"No files found")


        # Εκτέλεση της ανάλυσης για κάθε αρχείο
        results_data = []
        for file in files.values():
            start_time = time.time()  # Έναρξη χρονισμού
            results = codebert_sliding_window([file], 35, 35, 1, 25, model)  # Ανάλυση του αρχείου με CodeBERT
            end_time = time.time()  # Τερματισμός χρονισμού
            elapsed_time = end_time - start_time

            # Προετοιμασία δεδομένων για το αρχείο
            file_data = {
                "filename": file.filename,
                "sha": file.sha,
                "detected_kus": file.ku_results,  # Υποθέτω ότι το file έχει το αποτέλεσμα της ανάλυσης
                "elapsed_time": elapsed_time
            }
            results_data.append(file_data)
            print(file_data)  # Debugging output

        return results_data

    except Exception as e:
        print(f"Error during analysis: {e}")
        return {"error": str(e)}

