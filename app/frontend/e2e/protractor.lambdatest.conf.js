const {config: baseConfig} = require("./protractor.conf");
const LambdaTunnel = require("@lambdatest/node-tunnel");

exports.config = {
  ...baseConfig,

  lambdatestUser: process.env.LT_USERNAME,
  lambdatestKey: process.env.LT_ACCESS_KEY,

  directConnect: false,

  baseUrl: 'http://localhost:8080/app/',

  seleniumAddress: `https://${process.env.LT_USERNAME}:${process.env.LT_ACCESS_KEY}@hub.lambdatest.com/wd/hub`,

  capabilities: {
    "build": `PSA-${process.env.FULL_VERSION}`,
    "name": "PSA e2e",
    "platform": "MacOS Catalina",
    "browserName": "Safari",
    "version": "13.0",
    "resolution": "1440x900",
    "tunnel": true
  },

  beforeLaunch: async () => {
    const tunnelInstance = new LambdaTunnel();

    const tunnelArgs = {
      user: process.env.LT_USERNAME,
      key: process.env.LT_ACCESS_KEY,
      tunnelName: 'ci-automated-build'
    };

    try {
      await tunnelInstance.start(tunnelArgs);
      console.log("LambdaTest Tunnel is running");
    } catch (e) {
      console.log("Failed to start LambdaTest Tunnel", e);
    }
  }
};
