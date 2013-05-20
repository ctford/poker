Poker
=====

A macro-based refactoring library for Clojure.

Actually, there are no macros as such, but Poker uses
the macro approach of rewriting s-expressions to achieve
its transformations.

Design
------

Poker is intended to be invoked from an editor e.g.
[vim-fireplace](https://github.com/tpope/vim-fireplace).

It will not contain any editor-specific or REPL-specific
features.

Limitations
-----------

Poker will probably only support local refactorings.
Global renaming, inlining etc. are a little difficult.
