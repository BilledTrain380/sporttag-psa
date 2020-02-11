# Layout

A page always consists of a page title and one or more cards with a specific content.

## Simple Page
For pages which only require one card, the `app-simple-page` component should be used.
It is a simple wrapper for a row page which uses only one row and one column.

Usage
```
<app-simple-page pageTitle="My awesome page" cardTitle="My card">
    // Content of card
</app-simple-page>
```

## Row Page
For pages which require multiple cards, the `app-row-page` component should be used.
This component allows to create a row and column layout, where each column is a card.

Usage
```
<app-row-page pageTitle="My awesome page">
    <app-row-layout>
        <app-column-layout title="My card" size="6">
            // Content of card
        </app-column-layout-title>
    </app-row-layout>
</app-row-page>
```

* The `title` input is the card title to display
* The `size` input is a number value from bootstrap `col-lg-` css classes

## Layout Buttons
A card can define action buttons on its top left corner.

Usage
```
<app-layout-buttons>
    <app-layout-button [icon]="faUpload" buttonType="primary" text="Import"></app-layout-button>
</app-layout-buttons>
```

* The `icon` input is a font awesome icon reference
* The `buttonText` input has to be a bootstrap color value
* The `text` is the button text to display

> `app-layout-buttons` can be used inside a `app-column-layout` or `app-simple-page`.
