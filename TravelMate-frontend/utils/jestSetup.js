// utils/jestSetup.js

// Increase default timeout for all tests
jest.setTimeout(120000);

// Add custom matchers
expect.extend({
  toBeWithinRange(received, floor, ceiling) {
    const pass = received >= floor && received <= ceiling;
    if (pass) {
      return {
        message: () => `expected ${received} not to be within range ${floor} - ${ceiling}`,
        pass: true,
      };
    } else {
      return {
        message: () => `expected ${received} to be within range ${floor} - ${ceiling}`,
        pass: false,
      };
    }
  },
});

// Global beforeAll hook to set up common test environment
beforeAll(() => {
  console.log('\n=== Starting Jest Test Suite ===');
  console.log(`Test timestamp: ${new Date().toISOString()}`);
  console.log('Platform:', process.platform);
  console.log('Node version:', process.version);
  console.log('===============================\n');
});

// Global afterAll hook for cleanup
afterAll(() => {
  console.log('\n=== Test Suite Completed ===\n');
});

// Add better error handling
process.on('unhandledRejection', (reason, promise) => {
  console.error('Unhandled Rejection at:', promise, 'reason:', reason);
});

// Custom error formatter to improve error messages
const originalPrepareStackTrace = Error.prepareStackTrace;
Error.prepareStackTrace = (error, stack) => {
  if (process.env.NODE_ENV === 'test') {
    return `${error.message}\n${stack
      .filter(frame => !frame.getFileName()?.includes('node_modules'))
      .map(frame => `    at ${frame.getFunctionName() || 'anonymous'} (${frame.getFileName()}:${frame.getLineNumber()}:${frame.getColumnNumber()})`)
      .join('\n')}`;
  }
  return originalPrepareStackTrace(error, stack);
};