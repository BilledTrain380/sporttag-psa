const {config: baseConfig} = require("./protractor.conf");
const browserstack = require("browserstack-local");
const uuidv4 = require("uuid/v4");
const HttpClient = require("protractor-http-client").HttpClient;

exports.config = {
  ...baseConfig,

  directConnect: false,

  baseUrl: "http://localhost:8080/app/",

  seleniumAddress: "http://hub-cloud.browserstack.com/wd/hub",

  capabilities: {
    "project": "PSA",
    "build": `PSA-${process.env.SYSTEM_PULLREQUEST_SOURCEBRANCH}-${process.env.SYSTEM_STAGEATTEMPT}`,
    "name": `PSA-${process.env.FULL_VERSION}`,
    "browserstack.localIdentifier": uuidv4(),
    "browserstack.user": process.env.BROWSERSTACK_USER,
    "browserstack.key": process.env.BROWSERSTACK_KEY,
    "browserstack.local": true,
    "os": "OS X",
    "os_version": "Catalina",
    "browserName": "Safari",
    "browser_version": "13.0",
    "resolution": "1600x1200",
  },

  beforeLaunch: () => {
    console.log("Connecting browserstack local");
    return new Promise(((resolve, reject) => {
      const bsLocalOptions = {
        key: exports.config.capabilities["browserstack.key"],
        localIdentifier: exports.config.capabilities["browserstack.localIdentifier"],
      };
      exports.bs_local = new browserstack.Local();
      exports.bs_local.start(bsLocalOptions, error => {
        if (error) {
          return reject(error);
        }

        console.log("Connected. Now testing...");
        resolve();
      })
    }));
  },

  onPrepare: async () => {
    console.log("Waiting for PSA to run...");

    await browser.waitForAngularEnabled(false);

    return new Promise(((resolve, reject) => {
      const attempts = 3;
      let attemptCount = 0;

      const intervalId = setInterval(async () => {
        try {
          attemptCount++;
          console.log("Attempt to wait for psa health check: attempt", attemptCount);

          const http = new HttpClient("http://localhost:8080");
          http.failOnHttpError = true;

          const response = http.get("/actuator/health");
          const body = await response.jsonBody.get("status");

          if (body === "UP") {
            console.log("PSA is ready");
            resolve();
          }
        } catch (e) {
          console.log("Failed to parse health check");
        } finally {
          if (attemptCount === attempts) {
            clearInterval(intervalId);
            console.log("Failed to load psa health check");
            reject();
          }
        }
      }, 10000);
    })).then(() => baseConfig.onPrepare());
  },

  afterLaunch: () => {
    return new Promise((resolve => {
      exports.bs_local.stop(resolve);
    }));
  }
};
