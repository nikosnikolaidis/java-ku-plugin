import os
import shutil

from git import Repo

from config.settings import settings_instance
from core.git_operations import get_repo
from .diff import get_contributions_from_diffs


def create_temp_dir():
    if os.path.exists(settings_instance.TEMP_FILES_BASE_PATH):
        shutil.rmtree(settings_instance.TEMP_FILES_BASE_PATH)
    os.mkdir(settings_instance.TEMP_FILES_BASE_PATH)

def find_commit_by_sha(repo_path, sha):
    """
    Βρίσκει το commit με το συγκεκριμένο SHA από το repository.
    """
    try:
        repo = Repo(repo_path)
        commit = repo.commit(sha)
        return commit
    except Exception as e:
        print(f"Error finding commit: {e}")
        return None


def extract_contributions(repo_path, commit_limit=None, skip=0, fetch_updates=False):
    print(repo_path)
    repo = get_repo(repo_path)
    if fetch_updates:
        repo.remotes.origin.fetch()
    processed_commits = set()

    create_temp_dir()

    contributions = []

    # Iterate over commits in the active branch
    i=1
    for commit in repo.iter_commits(max_count=commit_limit, skip=skip):
        print(i)
        print(commit_limit, skip)
        i = i + 1
        if commit.hexsha in processed_commits:
            continue

        if len(commit.parents) == 1:
            # This is a non-merge commit
            parent_commit = commit.parents[0]
            diffs = parent_commit.diff(commit, create_patch=True)
        elif len(commit.parents) == 0:
            # For the first commit, we don't have a parent commit
            diffs = commit.diff(None, create_patch=True)
        else:
            # This is a merge commit
            continue
        print("Continued ... ")

        contributions += get_contributions_from_diffs(commit, diffs)

    return contributions
