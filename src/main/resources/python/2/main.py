import argparse

from analyze_module import analyze
from config.settings import settings_instance


def main():
    # Δημιουργία του parser για την επεξεργασία των παραμέτρων εισόδου
    parser = argparse.ArgumentParser(description='Analyze a git repository at a specific commit using CodeBERT.')

    # Παράμετροι που θα δώσει ο χρήστης στη γραμμή εντολών
    parser.add_argument('--repoPath', required=True, help='URL του αποθετηρίου git.')
    parser.add_argument('--codebertPath', required=True, help='Path προς το μοντέλο CodeBERT.')

    # Ανάγνωση των επιχειρημάτων από τη γραμμή εντολών
    args = parser.parse_args()

    settings_instance.set_codebert_base_path(args.codebertPath)  # Ενημερώνουμε το CODEBERT_BASE_PATH

    # Κλήση της συνάρτησης analyze με τις παραμέτρους githubUrl, sha και codebertPath
    analyze(args.repoPath)


# Εκτέλεση της main() όταν εκτελείται το script
if __name__ == "__main__":
    main()
