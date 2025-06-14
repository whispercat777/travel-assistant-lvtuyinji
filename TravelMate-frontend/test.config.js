// test.config.js
const path = require('path');
const os = require('os');

// Detect environment and set appropriate paths
const isWindows = os.platform() === 'win32';
const homedir = os.homedir();

// Default paths for different platforms
const defaultPaths = {
  windows: {
    devToolsInstallPath: 'C:\\Program Files (x86)\\Tencent\\微信web开发者工具',
    cliPath: 'C:\\Program Files (x86)\\Tencent\\微信web开发者工具\\cli.bat'
  },
  mac: {
    devToolsInstallPath: '/Applications/wechatwebdevtools.app',
    cliPath: '/Applications/wechatwebdevtools.app/Contents/MacOS/cli'
  }
};

// User-specific project path from the current path in the file
const projectPath = isWindows
  ? 'C:\\Users\\35173\\WeChatProjects\\TravelMateTJ' 
  : path.join(homedir, 'WeChatProjects/TravelMateTJ');

// Platform-specific defaults
const platformDefaults = isWindows ? defaultPaths.windows : defaultPaths.mac;

module.exports = {
  // Project path 
  projectPath: projectPath,
  
  // Developer tools installation path
  devToolsInstallPath: platformDefaults.devToolsInstallPath,
  
  // CLI path
  cliPath: platformDefaults.cliPath,
  
  // Port to connect to developer tools
  port: 11373,  // Make sure this matches the port in the IDE
  
  // Timeout settings (in milliseconds)
  timeout: 120000,  // Increase timeout to 2 minutes
  pageTimeout: 30000,  // Page operation timeout
  
  // Debug flags
  debug: true,  // Enable debug logging
  
  // Mock settings
  mockServerUrl: 'http://127.0.0.1:8080',  // Base URL for mock API endpoints
  
  // Testing flags
  runHeadless: false,  // Run tests in headless mode if supported
  
  // Jest config overrides (use these in jest.config.js)
  jestConfig: {
    testTimeout: 120000,  // Set Jest test timeout to 2 minutes
    verbose: true,
    setupFilesAfterEnv: [
      '<rootDir>/utils/setupMiniProgramTest.js'
    ]
  }
};