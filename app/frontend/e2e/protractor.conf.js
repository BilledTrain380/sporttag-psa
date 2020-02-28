// @ts-check
// Protractor configuration file, see link for more information
// https://github.com/angular/protractor/blob/master/lib/config.ts

const {SpecReporter} = require('jasmine-spec-reporter');

/**
 * @type { import("protractor").Config }
 */
exports.config = {
  allScriptsTimeout: 11000,
  specs: [
    './src/**/*.e2e-spec.ts'
  ],
  capabilities: {
    browserName: 'chrome'
  },

  directConnect: true,

  baseUrl: 'http://localhost:4200/',

  params: {
    username: "admin",
    password: "admin",
  },

  framework: 'jasmine',
  jasmineNodeOpts: {
    showColors: true,
    defaultTimeoutInterval: 30000,
    print: () => {
    }
  },
  onPrepare: async () => {
    require("ts-node").register({
      project: require('path').join(__dirname, './tsconfig.json')
    });

    jasmine.getEnv().addReporter(new SpecReporter({spec: {displayStacktrace: true}}));

    await browser.waitForAngularEnabled(false);

    console.log("Load base url: ", browser.baseUrl);
    await browser.driver.get(browser.baseUrl);

    console.log("Perform login");
    await browser.findElement(by.id("username")).sendKeys(browser.params.username);
    await browser.findElement(by.id("password")).sendKeys(browser.params.password);
    await browser.findElement(by.buttonText("Sign In")).click();

    browser.driver.sleep(250);

    await browser.waitForAngularEnabled(true);
  }
};
