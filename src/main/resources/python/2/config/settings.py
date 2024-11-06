import os

class Settings:
    def __init__(self):
        self.ROOT_DIR = os.path.dirname(os.path.abspath(os.path.join(__file__, "..")))
        self.TEMP_FILES_BASE_PATH = os.path.normpath(os.path.join(self.ROOT_DIR, "temp"))
        self.OUTPUT_FILES_BASE_PATH = os.path.normpath(os.path.join(self.ROOT_DIR, "output"))
        self.CLONED_REPO_BASE_PATH = os.path.normpath(os.path.join(self.ROOT_DIR, "cloned_repos"))
        self.MODELS_BASE_PATH = os.path.normpath(os.path.join(self.ROOT_DIR, "models", "binary_classifiers"))
        self.CODEBERT_BASE_PATH = os.path.normpath(os.path.join(self.ROOT_DIR, "models", "codebert"))
        self.FILE_TYPE = "java"
        self.MODELS_TO_LOAD = [
            "K2",
            "K3",
            "K4",
            "K6",
            "K7",
            "K8",
            "K9",
            "K10",
            "K11",
            "K12",
            "K13",
            "K14",
            "K15",
            "K16",
            "K17",
            "K18",
            "K19",
            "K20",
            "K21",
            "K22",
            "K23",
            "K24",
            "K25",
            "K27",
            "K28",
        ]

    # Δυναμική συνάρτηση για την ενημέρωση του CODEBERT_BASE_PATH
    def set_codebert_base_path(self, new_path):
        self.CODEBERT_BASE_PATH = new_path


settings_instance = Settings()