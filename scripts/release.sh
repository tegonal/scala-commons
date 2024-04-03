#!/usr/bin/env bash
#
#    __                          __
#   / /____ ___ ____  ___  ___ _/ /       This script is provided to you by https://github.com/tegonal/scripts
#  / __/ -_) _ `/ _ \/ _ \/ _ `/ /        Copyright 2022 Tegonal Genossenschaft <info@tegonal.com>
#  \__/\__/\_, /\___/_//_/\_,_/_/         It is licensed under European Union Public License v. 1.2
#         /___/                           Please report bugs and contribute back your improvements
#
#                                         Version: v0.1.0-SNAPSHOT
###################################
set -euo pipefail
shopt -s inherit_errexit
unset CDPATH
SCALA_COMMONS_VERSION="v0.1.0-SNAPSHOT"

if ! [[ -v scriptsDir ]]; then
	scriptsDir="$(cd -- "$(dirname -- "${BASH_SOURCE[0]:-$0}")" >/dev/null && pwd 2>/dev/null)"
	readonly scriptsDir
fi

if ! [[ -v projectDir ]]; then
	projectDir="$(realpath "$scriptsDir/../")"
	readonly projectDir
fi

if ! [[ -v dir_of_github_commons ]]; then
	dir_of_github_commons="$projectDir/.gt/remotes/tegonal-gh-commons/lib/src"
	readonly dir_of_github_commons
fi

if ! [[ -v dir_of_tegonal_scripts ]]; then
	dir_of_tegonal_scripts="$scriptsDir/../lib/tegonal-scripts/src"
	source "$dir_of_tegonal_scripts/setup.sh" "$dir_of_tegonal_scripts"
fi
sourceOnce "$dir_of_github_commons/gt/pull-hook-functions.sh"
sourceOnce "$dir_of_tegonal_scripts/releasing/release-template.sh"
sourceOnce "$scriptsDir/before-pr.sh"
sourceOnce "$scriptsDir/prepare-next-dev-cycle.sh"

function release() {
	source "$dir_of_tegonal_scripts/releasing/common-constants.source.sh" || die "could not source common-constants.source.sh"

	local version
	# shellcheck disable=SC2034   # they seem unused but are necessary in order that parseArguments doesn't create global readonly vars
	local branch nextVersion prepareOnly
	# shellcheck disable=SC2034   # is passed by name to parseArguments
	local -ra params=(
		version "$versionParamPattern" "$versionParamDocu"
		branch "$branchParamPattern" "$branchParamDocu"
		nextVersion "$nextVersionParamPattern" "$nextVersionParamDocu"
		prepareOnly "$prepareOnlyParamPattern" "$prepareOnlyParamDocu"
	)
	parseArguments params "" "$SCALA_COMMONS_VERSION" "$@"
	# we don't check if all args are set (and neither set default values) as we currently don't use
	# any param in here but just delegate to releaseTemplate.

	function release_afterVersionHook() {
		local version projectsRootDir additionalPattern
		parseArguments afterVersionHookParams "" "$SCALA_COMMONS_VERSION" "$@"

		local -r githubUrl="https://github.com/tegonal/scala-commons"
		replaceTagInPullRequestTemplate "$projectsRootDir/.github/PULL_REQUEST_TEMPLATE.md" "$githubUrl" "$version" || die "could not fill the placeholders in PULL_REQUEST_TEMPLATE.md"

		local -r versionWithoutLeadingV="${version:1}"

		perl -0777 -i \
			-pe "s@(ThisBuild / version := )\"[^\"]+\"@\${1}\"$versionWithoutLeadingV\"@g;" \
			"$projectsRootDir/build.sbt"

		perl -0777 -i \
			-pe "s@(libraryDependencies\s*\+=\s*\"com\.tegonal\"\s*%%\s*\"scala-commons\"\s*%\s*)\"[^\"]+\"@\${1}\"$versionWithoutLeadingV\"@g;" \
			"$projectsRootDir/README.md"
	}

	function release_releaseHook() {
		echo "Please enter the sonatype user token (input is hidden)"
		read -r -s SONATYPE_USER
		echo "Please enter the sonatype access token (input is hidden)"
		read -r -s SONATYPE_PW
		SONATYPE_PW="$SONATYPE_PW" SONATYPE_USER="$SONATYPE_USER" sbt publishSigned
	}

	# similar as in prepare-next-dev-cycle.sh, you might need to update it there as well if you change something here
	local -r additionalPattern="(SCALA_COMMONS_(?:LATEST_)?VERSION=['\"])[^'\"]+(['\"])"

	releaseTemplate \
		--project-dir "$projectDir" \
		--pattern "$additionalPattern" \
		"$@" \
		--after-version-update-hook release_afterVersionHook \
		--release-hook release_releaseHook
}

${__SOURCED__:+return}
release "$@"
