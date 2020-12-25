const {config: baseConfig} = require("./protractor.conf");
const browserstack = require("browserstack-local");
const uuidv4 = require("uuid/v4");
const HttpClient = require("protractor-http-client").HttpClient;

const pullRequestSourceBranch = process.env.SYSTEM_PULLREQUEST_SOURCEBRANCH;
const branch = pullRequestSourceBranch ? pullRequestSourceBranch : process.env.BUILD_SOURCEBRANCHNAME;

exports.config = {
  ...baseConfig,

  directConnect: false,

  baseUrl: "http://127.0.0.1:8080/app/en",

  seleniumAddress: "http://hub-cloud.browserstack.com/wd/hub",

  capabilities: {
    "project": "PSA",
    "build": `PSA-${branch}-${process.env.SYSTEM_STAGEATTEMPT}`,
    "name": `PSA-${process.env.FULL_VERSION}`,
    "browserstack.localIdentifier": uuidv4(),
    "browserstack.user": process.env.BROWSERSTACK_USER,
    "browserstack.key": process.env.BROWSERSTACK_KEY,
    "browserstack.local": true,
    "os": "Windows",
    "os_version": "10",
    "browserName": "Chrome",
    "resolution": "1600x1200",
  },

  beforeLaunch: () => {
    console.log("Connecting browserstack local");
    return new Promise((resolve, reject) => {
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
    });
  },

  onPrepare: async () => {
    console.log("Waiting for PSA to run...");

    await browser.waitForAngularEnabled(false);

    const attempts = 3;
    let attemptCount = 0;
    let healthCheckSuccessful = false;

    const checkHealth = async () => {
      try {
        attemptCount++;
        console.log("Attempt to wait for psa health check: attempt", attemptCount);

        const http = new HttpClient("http://localhost:8080");
        http.failOnHttpError = true;

        const response = http.get("/actuator/health");
        const body = await response.jsonBody.get("status");

        if (body === "UP") {
          console.log("PSA is ready");
          healthCheckSuccessful = true;
        }
      } catch (e) {
        console.log("Failed to parse health check", e);
      } finally {
        if (attemptCount === attempts) {
          console.log("Failed to load psa health check");
        } else if (!healthCheckSuccessful) {
          await checkHealth();
        }
      }
    };

    return checkHealth().then(() => baseConfig.onPrepare());
  },

  afterLaunch: () => {
    return new Promise((resolve => {
      exports.bs_local.stop(resolve);
    }));
  }
};
