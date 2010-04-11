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
    and extend `LazyWizardComposite` for your page widgets.
  * Useful callbacks for each of your pages: use methods like `beforeShow()`
    and `beforeNext()` to do setup, cleanup, validation and more in your
    pages. Cancel page navigation via the provided `NavigationEvent` if
    you want to cancel a page transition. Set up two-way links between pages
    based on user input.
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
[the GitHub wiki for GWT-Wizard][2].

  [2]: http://wiki.github.com/BinaryMuse/gwt-wizard/ "GWT-Wizard GitHub Wiki"