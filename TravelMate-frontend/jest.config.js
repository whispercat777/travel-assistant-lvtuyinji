// jest.config.js
const { jestConfig } = require('./test.config');

module.exports = {
  // Automatically clear mock calls and instances between every test
  clearMocks: true,

  // The directory where Jest should output its coverage files
  coverageDirectory: "coverage",

  // A list of paths to modules that run some code to configure the testing framework
  setupFilesAfterEnv: [
    "<rootDir>/utils/jestSetup.js"
  ],

  // The test environment that will be used for testing
  testEnvironment: "node",

  // The glob patterns Jest uses to detect test files
  testMatch: [
    "**/test/**/*.test.js"
  ],

  // An array of regexp pattern strings that are matched against all test paths
  testPathIgnorePatterns: [
    "/node_modules/"
  ],

  // Timeout for each test in milliseconds
  testTimeout: 120000,

  // Verbose output
  verbose: true,

  // Custom reporters
  reporters: [
    "default",
    ["./utils/customReporter.js", {}]
  ],

  // Override with test.config.js values
  ...jestConfig
};