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
    "./src/app.e2e-spec.ts",
    "./src/**/*.bpc.e2e-spec.ts",
    "./src/**/close-participation.e2e-spec.ts",
    "./src/**/*.apc.e2e-spec.ts",
    "./src/**/reset-participation.e2e-spec.ts",
    "./src/**/*.e2e-spec.ts"
  ],
  capabilities: {
    browserName: "chrome"
  },

  directConnect: true,

  baseUrl: "http://127.0.0.1:4200",

  params: {
    username: "admin",
    password: "admin",
    psaLoginUrl: "http://127.0.0.1:8080/login",
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

    console.log("Load login page: ", browser.params.psaLoginUrl);
    await browser.driver.get(browser.params.psaLoginUrl);

    browser.driver.sleep(500);

    console.log("Perform login");
    await browser.findElement(by.id("username")).sendKeys(browser.params.username);
    await browser.findElement(by.id("password")).sendKeys(browser.params.password);
    await browser.findElement(by.buttonText("Sign In")).click();

    browser.driver.sleep(500);

    console.log("Load psa application");
    // await browser.driver.get(browser.baseUrl);

    console.log("Wait for implicit flow redirect")
    await browser.driver.sleep(5000);

    await browser.waitForAngularEnabled(true);
  }
};
