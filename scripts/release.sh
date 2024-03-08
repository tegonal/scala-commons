#!/usr/bin/env bash
#
#    __                          __
#   / /____ ___ ____  ___  ___ _/ /       This script is provided to you by https://github.com/tegonal/scripts
#  / __/ -_) _ `/ _ \/ _ \/ _ `/ /        Copyright 2022 Tegonal Genossenschaft <info@tegonal.com>
#  \__/\__/\_, /\___/_//_/\_,_/_/         It is licensed under Apache License 2.0
#         /___/                           Please report bugs and contribute back your improvements
#
#                                         Version: v1.4.0-SNAPSHOT
###################################
set -euo pipefail
shopt -s inherit_errexit
unset CDPATH
export TEGONAL_SCRIPTS_VERSION='v1.4.0-SNAPSHOT'

if ! [[ -v scriptsDir ]]; then
	scriptsDir="$(cd -- "$(dirname -- "${BASH_SOURCE[0]:-$0}")" >/dev/null && pwd 2>/dev/null)"
	readonly scriptsDir
fi

if ! [[ -v projectDir ]]; then
	projectDir="$(realpath "$scriptsDir/../")"
	readonly projectDir
fi

if ! [[ -v dir_of_tegonal_scripts ]]; then
	dir_of_tegonal_scripts="$scriptsDir/../lib/tegonal-scripts/src"
	source "$dir_of_tegonal_scripts/setup.sh" "$dir_of_tegonal_scripts"
fi
sourceOnce "$dir_of_tegonal_scripts/releasing/release-files.sh"
sourceOnce "$dir_of_tegonal_scripts/utility/checks.sh"

function release() {
	local projectsRootDirParamPatternLong additionalPatternParamPatternLong prepareOnlyParamPatternLong
	source "$dir_of_tegonal_scripts/releasing/shared-patterns.source.sh" || die "could not source shared-patterns.source.sh"


	# similar as in prepare-next-dev-cycle.sh, you might need to update it there as well if you change something here
	local -r additionalPattern="(SCALA_COMMONS_(?:LATEST_)?VERSION=['\"])[^'\"]+(['\"])"

	local prepareOnly
	# shellcheck disable=SC2034   # is passed by name to parseArguments
	local -ra params=(
		prepareOnly "$prepareOnlyParamPattern" ''
	)

	parseArgumentsIgnoreUnknown params "" "$TEGONAL_SCRIPTS_VERSION" "$@"

	preReleaseCheckGit "$@"

	# shellcheck disable=SC2310			# we are aware of that || will disable set -e for sourceOnce
	sourceOnce "$scriptsDir/before-pr.sh" || die "could not source before-pr.sh"

	# make sure everything is up-to-date and works as it should
	beforePr || return $?

	updateVersionCommonSteps \
		"$projectsRootDirParamPatternLong" "$projectDir" \
		"$additionalPatternParamPatternLong" "$additionalPattern" \
		"$@"

	# run again since we made changes
	beforePr || return $?

	echo "Please enter the sonatype user token (and press ENTER)"
	read -r -s SONATYPE_USER
	echo "Please enter the sonatype access token (and press ENTER)"
	read -r -s SONATYPE_PW
	SONATYPE_PW="$SONATYPE_PW" SONATYPE_USER="$SONATYPE_USER" sbt publishSigned

	if [[ $prepareOnly != true ]]; then
		releaseTagPushAndPrepareNext "$@"
	else
		printf "\033[1;33mskipping commit, creating tag and prepare-next-dev-cycle due to %s\033[0m\n" "$prepareOnlyParamPatternLong"
	fi

}

${__SOURCED__:+return}
release "$@"
