/**
 * Original work:
 *   https://github.com/meanmail-dev/nginx-intellij-plugin
 *   Copyright (c) 2020 meanmail.dev
 *   Licensed under the MIT License
 */

package cc.ayakurayuki.psi

interface WithPathElement : NamedElement {
    val path: List<String>
}
