Poker
=====

[![Build Status](https://travis-ci.org/ctford/poker.png)](https://travis-ci.org/ctford/poker)

Poker implements refactorings as transformations of s-expressions.

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
