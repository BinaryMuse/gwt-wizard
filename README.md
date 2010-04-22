GWT-Wizard: A GWT Wizard Widget for Your Project
================================================

GWT-Wizard is a [wizard][1] widget for use in your own GWT projects. It
tries to make as few assumptions as possible about your needs, making it a
flexible and powerful tool, while still providing sane defaults that allow
simple projects to get up and running with as little configuration as possible.

  [1]: http://en.wikipedia.org/wiki/Wizard_%28software%29 "Wizard on Wikipedia"

![sample-wizard](http://binarymuse.github.com/gwt-wizard/images/sample-wizard.png)

GWT-Wizard has a few nice features, including the following:

  * Flexible view component: use the built in view, or provide your own by
    implementing provided interfaces.
  * Support for lazy-loading wizard pages: if you don't want to attach your
    wizard's pages to the DOM right away, enable lazy loading and attachment
    and extend `LazyComposite` for your page widgets.
  * Useful transition hooks for each of your pages: use methods like
    `beforeShow()` and `beforeNext()` to do setup, cleanup, validation and
    more in your pages. Cancel page navigation via the provided
    `NavigationEvent` if you want to cancel a page transition. Set up two-way
    links between pages based on user input.
  * Flexible behavior with sane defaults: GWT-Wizard lets you customize
    virtually every element of your wizard, from how it looks to how it
    behaves. But, if you just want a simple, linear wizard, you don't have
    to customize anything.

Getting Started
---------------

Getting started with GWT-Wizard is easy:

  1. Put `gwt-wizard.jar` on your project's build path
  2. Inherit the wizard module with `<inherits name='net.binarymuse.gwt.Wizard' />`
  2. Extend `WizardContext` to define your context object
  3. Create one or more page for your wizard (a single-page wizard seems kinda
     silly) by extending `WizardPage` (the only methods you **must** provide
     are `getTitle()`, `getPageID()`, and `asWidget()`)
  4. Create a new wizard with `new Wizard("Wizard Title", contextObject)` (you
     can also pass a custom view if you so wish)
  5. Call `wizard.addPage()` for each of the pages you wish to add to your wizard
  6. Attach the wizard to the DOM

More Details and Examples
-------------------------

You can find code samples, working examples, and more at the
[GWT-Wizard's home on the web][2].

  [2]: http://gwt-wizard.binarymuse.net/ "GWT-Wizard Home Page"

Project Status
--------------

GWT-Wizard should be considered alpha software. The API is highly malleable
and may change from commit to commit.

Building GWT-Wizard from Source
-------------------------------

You can compile GWT-Wizard by running `ant build` from the repo root. You
can create a JAR file (`gwt-wizard.jar`) by running `ant jar` from the same
location. You must specify the location of the GWT JARs in an environment
variable called `GWTPATH`, or else update `build.xml` to change all instances
of `"${env.GWTPATH}"` to the location of the JARs.

`ant clean` will delete the `bin` directory, but not `gwt-wizard.jar`.

Generating the Documentation
----------------------------

You can generate a copy of the project's site
([http://gwt-wizard.binarymuse.net][2]) by doing the following:

  1. Install [nanoc][3]
  2. `cd pages-source` from the repo root
  3. `./compile`

If the project's JavaDoc output is located in `../doc` (in a folder called
`doc` from the repo root), the compile script will copy the JavaDoc as it
compiles the site. The output will be placed in `pages-source/output`.

  [3]: http://nanoc.stoneship.org/ "nanoc"

What's Missing
--------------

There are a few things missing from the current iteration of GWT-Wizard:

  * Removing pages: once a page has been added to a wizard, it can't
    be removed
  * DOM dependence: there is some DOM/GWT dependence in classes that should
    be a presenter (in the MVP sense)
  * Unit Tests: there are currently no unit tests checked in for the project

License
-------

GWT-Wizard is licensed under the MIT License. See the LICENSE file for details.
