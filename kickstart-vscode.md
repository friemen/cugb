# Setup for Clojure Development with Visual Studio Code

The following sections give you a quick start if you're new to Visual
Studio Code (VSC) and Clojure development.


## Installed Extensions

We provided a bundle of Java 8 and VSC with the following extensions
already installed:

- Clojure 0.10.1
- Paredit 0.1.1


## Getting started

To create a new Clojure project use `lein new practising`.

Start VSC, press <bbd>Ctrl+k Ctrl+o</kbd>, select folder `practising`
and open the file `practising/core.clj`.

The Clojure extension automatically starts a fresh REPL and connects
to it.


## General shortcuts

The following shortcuts are the most important to master very
basic tasks in VSC:

Shortcut                  | Description
---                       | ---
<kbd>Ctrl+q</kbd>         | Quit VSC
<kbd>Ctrl+Shift+P</kbd>   | Show all available commands
<kbd>Ctrl+k Ctrl+s</kbd>  | Open keybindings definition
<kbd>Ctrl+k Ctrl+o</kbd>  | Open project folder
<kbd>Ctrl+k f</kbd>       | Close project folder
<kbd>Ctrl+Tab</kbd>       | Cycle through editors
<kbd>Ctrl+PageDown</kbd>  | Switch to next editor
<kbd>Ctrl+PageUp</kbd>    | Switch to previous editor
<kbd>Alt+1 .. 9</kbd>     | Switch to tab with index 1 .. 9
<kbd>Ctrl+w</kbd>         | Close current tab
<kbd>Ctrl+Space</kbd>     | Open code completion
<kbd>Ctrl+s</kbd>         | Save file
<kbd>Ctrl+f</kbd>         | Find in file
<kbd>Ctrl+h</kbd>         | Find/replace in file

## Clojure related shortcuts

Shortcut                  | Description
---                       | ---
<kbd>Alt+x s</kbd>        | Select to bracket
<kbd>Alt+x d</kbd>        | Select defn
<kbd>Alt+x c</kbd>        | Eval and show result of file or selection
<kbd>Alt+x t</kbd>        | Run all tests for current namespace
<kbd>Alt+x f</kbd>        | Format file or selection
<kbd>Ctrl+Alt+.</kbd>     | Slurp forward
<kbd>Ctrl+Alt+,</kbd>     | Barf forward
<kbd>Ctrl+Left</kbd>      | Go to next Sexp on same level
<kbd>Ctrl+Right</kbd>     | Go to previous Sexp on same level
<kbd>Ctrl+Up</kbd>        | Go one nesting level up
<kbd>Ctrl+Down</kbd>      | Go one nesting level down
