#!/usr/bin/env bash
#
#    __                          __
#   / /____ ___ ____  ___  ___ _/ /       This file is provided to you by https://github.com/tegonal/oss-template
#  / __/ -_) _ `/ _ \/ _ \/ _ `/ /        It is licensed under European Union Public License 1.2
#  \__/\__/\_, /\___/_//_/\_,_/_/         Please report bugs and contribute back your improvements
#         /___/
#                                         Version: v0.1.0-SNAPSHOT
#
###################################
set -euo pipefail
shopt -s inherit_errexit
unset CDPATH

project_dir="$(cd -- "$(dirname -- "${BASH_SOURCE[0]:-$0}")" >/dev/null && pwd 2>/dev/null)"
readonly project_dir

if ! [[ -v dir_of_tegonal_scripts ]]; then
	dir_of_tegonal_scripts="$project_dir/lib/tegonal-scripts/src"
	source "$dir_of_tegonal_scripts/setup.sh" "$dir_of_tegonal_scripts"
fi
sourceOnce "$dir_of_tegonal_scripts/utility/log.sh"

printf "Please choose your license:\n(1) EUPL 1.2\n(2) AGPL 3\n(3) Apache 2.0\n(4) CC0 1.0 Universal\nYour selection (default (1) EUPL 1.2): "
read -r choice

if [[ -z "$choice" ]]; then
	choice=1
fi

if [[ choice -eq 1 ]]; then
	licenseUrl="https://joinup.ec.europa.eu/collection/eupl/eupl-text-11-12"
	licenseShortName="EUPL 1.2"
	licenseFullName="EUPL 1.2"
	cp "$project_dir/EUPL.LICENSE.txt" "$project_dir/LICENSE.txt"
elif [[ choice -eq 2 ]]; then
	licenseUrl="https://joinup.ec.europa.eu/collection/eupl/eupl-text-11-12"
	licenseShortName="AGPL 3"
	licenseFullName="GNU Affero General Public License v3"
	cp "$project_dir/AGPL.LICENSE.txt" "$project_dir/LICENSE.txt"
elif [[ choice -eq 3 ]]; then
	licenseUrl="https://www.apache.org/licenses/LICENSE-2.0"
	licenseShortName="Apache 2.0"
	licenseFullName="Apache License, Version 2.0"
	cp "$project_dir/Apache.LICENSE.txt" "$project_dir/LICENSE.txt"
elif [[ choice -eq 4 ]]; then
	licenseUrl="https://creativecommons.org/publicdomain/zero/1.0/"
	licenseShortName="CC0 1.0"
	licenseFullName="Creative Commons Zero v1.0 Universal"
	cp "$project_dir/CC0.LICENSE.txt" "$project_dir/LICENSE.txt"
else
	die "the selection %s is invalid, chose one between 1 and 4" "$choice"
fi

printf "Please insert the project name: "
read -r projectName
tmpName="${projectName//-/_}"
projectNameUpper="${tmpName^^}"

printf "Please insert the github name (default %s): " "$projectName"
read -r projectNameGithub

licenseBadge="[![$licenseShortName](https://img.shields.io/badge/%E2%9A%96-${licenseShortName// /%220}-%230b45a6)]($licenseUrl \"License\")"
licenseLink="[$licenseFullName]($licenseUrl)"

find "$project_dir" -type f \
	-not -path "$project_dir/lib" \
	-not -name "cleanup.yml" \
	-not -name "gt-update.yml" \
	-not -name "CODE_OF_CONDUCT.md" \
	\( -name "*.md" -o -name "*.txt" -o -name "*.yaml" -o -name "*.yml" \) \
	-print0 |
	while read -r -d $'\0' file; do
		PROJECT_NAME_UPPER="${projectNameUpper}" \
		PROJECT_NAME="$projectName" \
		PROJECT_NAME_GITHUB="$projectNameGithub" \
			LICENSE_BADGE="$licenseBadge" \
			LICENSE_LINK="$licenseLink" \
			LICENSE_FULLNAME="$licenseFullName" \
			YEAR=$(date +%Y) \
			perl -0777 -i \
			-pe "s@PROJECT_NAME_UPPER@\$ENV{PROJECT_NAME_UPPER}@g;" \
			-pe "s@PROJECT_NAME@\$ENV{PROJECT_NAME}@g;" \
			-pe "s@PROJECT_NAME_GITHUB@\$ENV{PROJECT_NAME_GITHUB}@g;" \
			-pe "s@LICENSE_BADGE@\$ENV{LICENSE_BADGE}@g;" \
			-pe "s@LICENSE_LINK@\$ENV{LICENSE_LINK}@g;" \
			-pe "s@LICENSE_FULLNAME@\$ENV{LICENSE_FULLNAME}@g;" \
			-pe "s@YEAR@\$ENV{YEAR}@g;" \
			"$file"
	done

find "$project_dir" -maxdepth 1 -name "*.LICENSE.txt" -print0 |
	while read -r -d $'\0' license; do
		rm "$license"
	done

logSuccess "initialised the repository, please follow the remaining steps in README.md"
