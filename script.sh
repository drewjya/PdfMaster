#!/bin/bash

# 1. Fetch current tags so you know where you left off
echo "Latest tags:"
git tag --sort=-v:refname | head -n 5
echo "------------------------------"

# 2. Ask the user for the new version number
read -p "Enter new version (e.g., 1.0.6): " VERSION

# Ensure the version starts with 'v'
if [[ $VERSION != v* ]]; then
    VERSION="v$VERSION"
fi

# 3. Confirm the action
read -p "Create and push tag $VERSION? (y/n): " CONFIRM
if [[ $CONFIRM != [yY] ]]; then
    echo "Aborted."
    exit 1
fi

# 4. Run the Git commands
echo "Tagging $VERSION..."
git tag "$VERSION"

echo "Pushing $VERSION to origin..."
git push origin "$VERSION"

echo "Done! Version $VERSION is live."