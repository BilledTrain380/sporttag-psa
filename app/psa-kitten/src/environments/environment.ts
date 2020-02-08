/**
 * @license
 * Copyright Akveo. All Rights Reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */
// The file contents for the current environment will overwrite these during build.
// The build system defaults to the dev environment which uses `environment.ts`, but if you do
// `ng build --env=prod` then `environment.prod.ts` will be used instead.
// The list of which env maps to which file can be found in `.angular-cli.json`.

export const environment = {
    production: false,
    host: 'http://localhost:8080',
    devHost: 'http://localhost:8080', // host name for aot build can not be dynamic (@NgModule and such must use this)
    devServer: 'http://localhost:4200',
    baseHref: '/',
    passwordPolicy: '^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#$^+=!*()@%&]).[\\S]{8,64}$',
};
