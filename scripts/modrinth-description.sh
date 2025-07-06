#!/bin/bash
#
# modrinth-description.sh
# 
# Generates a Modrinth-compatible description from README.md
# Run this and copy the output to your Modrinth project description
#

echo "=== MODRINTH PROJECT DESCRIPTION ==="
echo "Copy the content below to your Modrinth project description:"
echo "====================================="
echo ""

# Remove the screenshot at the end and any GitHub-specific references
sed -e '/^!\[System\]/,$d' \
    -e 's|https://github.com/YOUR_USERNAME/splitscreen|https://github.com/YOUR_ACTUAL_USERNAME/splitscreen|g' \
    -e 's|YOUR_PROJECT_SLUG|your-actual-project-slug|g' \
    README.md

echo ""
echo "====================================="
echo "Remember to:"
echo "1. Replace 'YOUR_ACTUAL_USERNAME' with your GitHub username"
echo "2. Replace 'your-actual-project-slug' with your Modrinth project slug"
echo "3. Add screenshots to your Modrinth project page"
echo "4. Set appropriate tags/categories in Modrinth"
