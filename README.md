# nginx-configuration-formatter

A plugin for reformatting Nginx configuration file for IntelliJ-based IDEs.

## Features

- Reformat Nginx configuration file
- Nginx configuration file syntax highlight
- Folding blocks and lua blocks
- Line comment

## Usage

Search the plugin name `Nginx Configuration Formatter` at JetBrains Marketplace
and install it.

### IDE Reformat code - based on PSI Formatting Model

- Use `Code > Reformat code` to reformat Nginx Configuration File, shortcut
  examples are:
    - default on macOS is `⌘ Cmd + ⌥ Option + L`
    - default on Windows is `Ctrl + Alt + L`

### Action: Reformat - based on `nginxbeautifier`

> Ported and improved from
> [vasilevich/nginxbeautifier](https://github.com/vasilevich/nginxbeautifier),
> huge thanks to @vasilevich.

This reformatting approach is based on `vasilevich/nginxbeautifier` with
several optimizations. It essentially relies on `kotlin.String` and regular
expressions to perform line-by-line analysis of Nginx configuration files,
followed by formatting operations such as indentation and alignment.

- Use `Ctrl + Alt + Shift + N` as default shortcut to reformat Nginx
  Configuration File.
- (on macOS) Use `⌘ Cmd + ⌥ Option + ⇧ Shift + N` to reformat Nginx
  Configuration File.
- You can change the shortcut at `Settings > Keymap > Plugins > Nginx
  Configuration Formatter > Reformat`.

## License

This project is licensed under the Apache License 2.0.

```text
Copyright 2026 AyakuraYuki

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

### License Announcement

This project is inspired by the following projects, and modifications have been made.

- vasilevich/nginxbeautifier

    ```text
    Copyright 2016 yosef langer (vasilevich)

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
        http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
    ```

- meanmail-dev/nginx-intellij-plugin

    ```text
    MIT License

    Copyright (c) 2020 meanmail.dev
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
    ```
