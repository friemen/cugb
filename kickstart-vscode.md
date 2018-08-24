# Setup for Clojure Development with Visual Studio Code

The following sections give you a quick start if you're new to [Visual
Studio Code (VSC)](https://code.visualstudio.com/) and Clojure development.

## Installation

Copy and unzip/untar the provided archive. It contains a JDK 8, the Clojure
build tool [Leiningen](https://leiningen.org/) and VSC with the
following extensions pre-installed:

- [Clojure 0.10.1](https://marketplace.visualstudio.com/items?itemName=avli.clojure)
- [Paredit 0.1.1](https://marketplace.visualstudio.com/items?itemName=clptn.code-paredit)

You can do your work within the toplevel folder
`Clojure_Kickstart_Workshop_<OS>` that was created by unzip (Windows)
oder tar (Linux, MacOSX).


Leiningen includes a library dependency management system (based on
Maven 2) and will download all missing libraries into a local cache
located in `$HOME/.m2/repository`.  Our archive contains a
`.m2/repository` directory that you should copy into your user
directory to reduce the amount of data to be downloaded.

Open a terminal window.

To make commands like `lein`, `visual_studio_code` oder `java` and the
necessary environment variables available everywhere in the terminal
you should execute the provided `setenv.bat` (on Windows) or source
the `setenv` file (on Linux, MacOSX).


## Getting started

To create a new Clojure project execute for example `lein new
practising` in your terminal window.

Start VSC with the script `visual_studio_code`, press <bbd>Ctrl+k
Ctrl+o</kbd>, select folder `practising` and open the file
`src/practising/core.clj`.

The Clojure extension automatically starts a fresh REPL and connects
to it.

(To optionally connect to this REPL via an additional console go to
the VSC Terminal and type `lein repl :connect <port>`. The port is the one
that VSC prints in the bottom-left corner of the screen as
`nrep://127.0.0.1:<port>`.)

In `core.clj` you'll find a single function `foo`. Type <kbd>Alt+x
c</kbd> to compile the whole namespace.

To execute just this function you can simply add an expression like
`(foo "Fred")` to the bottom of core.clj. Type <kbd>Alt+x d</kbd> and
then <kbd>Alt+x c</kbd> to evaluate the selected expression.

Change the string in `foo`, again use the key sequence <kbd>Alt+x
d</kbd> followed by <kbd>Alt+x c</kbd> to compile it, go to your
invocation `(foo "Fred")` and evaluate that again. The output should
reflect your change.

Congrats, you have just mastered the basic workflow of Clojure
programming. You altered a live system while it is running.


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
<kbd>Alt+x z</kbd>        | Jump to terminal
<kbd>Ctrl+Alt+.</kbd>     | Slurp forward
<kbd>Ctrl+Alt+,</kbd>     | Barf forward
<kbd>Ctrl+Left</kbd>      | Go to next Sexp on same level
<kbd>Ctrl+Right</kbd>     | Go to previous Sexp on same level
<kbd>Ctrl+Up</kbd>        | Go one nesting level up
<kbd>Ctrl+Down</kbd>      | Go one nesting level down
