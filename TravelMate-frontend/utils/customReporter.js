// utils/customReporter.js
class CustomReporter {
  constructor(globalConfig, reporterOptions) {
    this._globalConfig = globalConfig;
    this._options = reporterOptions;
    this.startTime = Date.now();
    this.testResults = [];
  }

  onRunStart(results, options) {
    console.log('\n🚀 Starting Mini Program Test Suite...');
    console.log(`Time: ${new Date().toLocaleTimeString()}`);
    console.log('-'.repeat(50));
  }

  onTestStart(test) {
    console.log(`\n▶️ Running test: ${test.path}`);
  }

  onTestResult(test, testResult, aggregatedResult) {
    const { numFailingTests, numPassingTests, numPendingTests, testResults } = testResult;
    
    this.testResults.push({
      path: test.path,
      passed: numFailingTests === 0,
      numPassingTests,
      numFailingTests,
      numPendingTests,
      testResults
    });
    
    console.log(`\n📋 Test file: ${test.path}`);
    console.log(`   ✅ Passed: ${numPassingTests}`);
    console.log(`   ❌ Failed: ${numFailingTests}`);
    console.log(`   ⏸️ Skipped: ${numPendingTests}`);
    
    // Print test details
    testResults.forEach(result => {
      const status = result.status === 'passed' ? '✅' : result.status === 'failed' ? '❌' : '⏸️';
      console.log(`   ${status} ${result.title} [${result.duration}ms]`);
      
      if (result.status === 'failed') {
        console.log('\n   Error details:');
        console.log(`   ${result.failureMessages.join('\n   ')}`);
      }
    });
  }

  onRunComplete(contexts, results) {
    const duration = (Date.now() - this.startTime) / 1000;
    
    console.log('\n='.repeat(50));
    console.log('📊 Test Summary:');
    console.log(`   Total test suites: ${results.numTotalTestSuites}`);
    console.log(`   Total tests: ${results.numTotalTests}`);
    console.log(`   Passed tests: ${results.numPassedTests}`);
    console.log(`   Failed tests: ${results.numFailedTests}`);
    console.log(`   Skipped tests: ${results.numPendingTests}`);
    console.log(`   Time: ${duration.toFixed(2)}s`);
    
    // Overall status
    if (results.numFailedTests > 0) {
      console.log('\n❌ Some tests failed!');
    } else {
      console.log('\n✅ All tests passed!');
    }
    console.log('='.repeat(50));
    
    // Display most common errors
    if (results.numFailedTests > 0) {
      this._printCommonErrors();
    }
  }
  
  _printCommonErrors() {
    console.log('\n🔍 Common Error Patterns:');
    
    // Extract all error messages
    const errorMessages = [];
    this.testResults.forEach(fileResult => {
      fileResult.testResults.forEach(testResult => {
        if (testResult.status === 'failed') {
          testResult.failureMessages.forEach(msg => {
            // Extract the core error message without stack trace
            const coreError = msg.split('\n')[0];
            errorMessages.push(coreError);
          });
        }
      });
    });
    
    // Count error occurrences
    const errorCounts = {};
    errorMessages.forEach(msg => {
      errorCounts[msg] = (errorCounts[msg] || 0) + 1;
    });
    
    // Sort and display most common errors
    const sortedErrors = Object.entries(errorCounts)
      .sort((a, b) => b[1] - a[1])
      .slice(0, 5);
    
    if (sortedErrors.length > 0) {
      sortedErrors.forEach(([error, count]) => {
        console.log(`   • ${error} (${count} occurrences)`);
      });
      
      // Provide troubleshooting tips
      this._printTroubleshootingTips(sortedErrors);
    } else {
      console.log('   No error patterns detected');
    }
  }
  
  _printTroubleshootingTips(errors) {
    console.log('\n🔧 Troubleshooting Suggestions:');
    
    const errorTypes = errors.map(([error]) => error.toLowerCase());
    
    if (errorTypes.some(err => err.includes('connect') || err.includes('connection'))) {
      console.log('   • Connection issues detected:');
      console.log('     - Make sure WeChat DevTools is running and the service port is enabled');
      console.log('     - Check if port 11373 is correctly configured in test.config.js');
      console.log('     - Try running "cli.bat open --project [projectPath]" manually first');
    }
    
    if (errorTypes.some(err => err.includes('timeout'))) {
      console.log('   • Timeout issues detected:');
      console.log('     - Increase timeout values in test.config.js');
      console.log('     - Add more await page.waitFor() calls before interactions');
    }
    
    if (errorTypes.some(err => err.includes('element') || err.includes('selector'))) {
      console.log('   • Element selection issues detected:');
      console.log('     - Verify element selectors match your WXML structure');
      console.log('     - Try using more generic selectors or multiple selection strategies');
      console.log('     - Add console.log(await page.$(\'*\')); to debug available elements');
    }
    
    if (errorTypes.some(err => err.includes('evaluate') || err.includes('execution'))) {
      console.log('   • Script execution issues detected:');
      console.log('     - Check for JS errors in your Mini Program');
      console.log('     - Ensure mock implementations match the expected API');
    }
    
    console.log('\n   For more help, visit: https://developers.weixin.qq.com/miniprogram/dev/devtools/auto/');
  }
}

module.exports = CustomReporter;