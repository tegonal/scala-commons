//
//    __                          __
//   / /____ ___ ____  ___  ___ _/ /       This file is provided to you by https://github.com/tegonal/scala-commons
//  / __/ -_) _ `/ _ \/ _ \/ _ `/ /        Copyright 2024 Tegonal Genossenschaft <info@tegonal.com>
//  \__/\__/\_, /\___/_//_/\_,_/_/         It is licensed under Apache 2.0
//         /___/                           Please report bugs and contribute back your improvements
//
//                                         Version: v0.17.0
//##################################
const {MatrixBuilder} = require('./matrix_builder');
const {configureScalaDefaults, setMatrix} = require('./matrix_commons');

const matrix = new MatrixBuilder();
configureScalaDefaults(matrix)

setMatrix(matrix, 4);
