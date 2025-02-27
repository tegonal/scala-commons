#!/usr/bin/env bash
#
#    __                          __
#   / /____ ___ ____  ___  ___ _/ /       This script is provided to you by https://github.com/tegonal/scala-commons
#  / __/ -_) _ `/ _ \/ _ \/ _ `/ /        Copyright 2024 Tegonal Genossenschaft <info@tegonal.com>
#  \__/\__/\_, /\___/_//_/\_,_/_/         It is licensed under Apache License 2.0
#         /___/                           Please report bugs and contribute back your improvements
#
#                                         Version: v0.3.0-SNAPSHOT
###################################
set -euo pipefail
shopt -s inherit_errexit
unset CDPATH
SCALA_COMMONS_LATEST_VERSION="v0.2.0"

if ! [[ -v dir_of_tegonal_scripts ]]; then
	dir_of_tegonal_scripts="$(cd -- "$(dirname -- "${BASH_SOURCE[0]:-$0}")" >/dev/null && pwd 2>/dev/null)/../../src"
	source "$dir_of_tegonal_scripts/setup.sh" "$dir_of_tegonal_scripts"
fi

if ! [[ -v dir_of_github_commons ]]; then
	dir_of_github_commons="$(cd -- "$(dirname -- "${BASH_SOURCE[0]:-$0}")" >/dev/null && pwd 2>/dev/null)/lib/src"
	readonly dir_of_github_commons
fi

sourceOnce "$dir_of_github_commons/gt/pull-hook-functions.sh"
sourceOnce "$dir_of_tegonal_scripts/utility/parse-fn-args.sh"

function gt_pullHook_tegonal_gh_commons_before() {
	local _tag source _target
	# shellcheck disable=SC2034   # is passed to parseFnArgs by name
	local -ra params=(_tag source _target)
	parseFnArgs params "$@"

	replaceTegonalGhCommonsPlaceholders_Tegonal "$source" "scala-commons" "$SCALA_COMMONS_LATEST_VERSION" "scala-commons"

	if [[ $source == */cleanup.yml ]]; then
		local -r yaml=$(
				# shellcheck disable=SC2312		# cat shouldn't fail for a constant string hence fine to ignore exit code
				cat <<-EOM
					      # start inserted via pull-hook.sh - modify there
					      - name: Setup JDK 17
					        uses: actions/setup-java@v4
					        with:
					          distribution: temurin
					          java-version: 17
					          cache: sbt
					      - uses: sbt/setup-sbt@v1
					      # end inserted via pull-hook.sh - modify there
				EOM
		)
		YAML="$yaml" perl -0777 -i -pe "s|\n(\s+ - name: Cleanup Sources)|\n\$ENV{YAML}\n\${1}|g" "$source"
	fi
}

function gt_pullHook_tegonal_gh_commons_after() {
	# no op, nothing to do
	true
}
