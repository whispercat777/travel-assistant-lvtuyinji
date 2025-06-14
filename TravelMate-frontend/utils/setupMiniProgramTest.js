// utils/setupMiniProgramTest.js
const automator = require('miniprogram-automator');
const path = require('path');
const { exec } = require('child_process');
const util = require('util');
const execPromise = util.promisify(exec);

async function setupMiniProgramTest() {
  // 尝试从项目根目录加载测试配置
  let config;
  try {
    config = require('../test.config');
  } catch (error) {
    console.log('无法加载测试配置，使用默认配置:', error.message);
    config = {
      projectPath: 'C:\\Users\\35173\\WeChatProjects\\TravelMateTJ',
      cliPath: 'C:\\Program Files (x86)\\Tencent\\微信web开发者工具\\cli.bat',
      port: 11373,
      timeout: 60000
    };
  }

  console.log('=== 微信小程序测试设置 ===');
  console.log(`项目路径: ${config.projectPath}`);
  console.log(`开发者工具路径: ${config.cliPath}`);
  console.log(`端口号: ${config.port}`);
  
  // 首先尝试使用命令行确保自动化是启用的
  console.log('\n1. 尝试启用自动化模式...');
  try {
    // 首先使用 open 命令打开项目
    console.log('1.1 打开项目...');
    const openCmd = `"${config.cliPath}" open --project "${config.projectPath}"`;
    console.log('执行命令:', openCmd);
    const { stdout: openStdout } = await execPromise(openCmd);
    console.log('打开项目结果:', openStdout);
    
    // 等待一段时间让项目完全加载
    console.log('等待项目加载 (5秒)...');
    await new Promise(resolve => setTimeout(resolve, 5000));
    
    // 然后使用 auto 命令启用自动化
    console.log('1.2 启用自动化...');
    const autoCmd = `"${config.cliPath}" auto --project "${config.projectPath}" --auto-port ${config.port}`;
    console.log('执行命令:', autoCmd);
    const { stdout: autoStdout } = await execPromise(autoCmd);
    console.log('启用自动化结果:', autoStdout);
    
    // 再次等待让自动化生效
    console.log('等待自动化生效 (5秒)...');
    await new Promise(resolve => setTimeout(resolve, 5000));
  } catch (cliError) {
    console.log('命令行操作失败 (这不一定是致命错误，继续尝试连接):', cliError.message);
    if (cliError.stdout) console.log('标准输出:', cliError.stdout);
    if (cliError.stderr) console.log('错误输出:', cliError.stderr);
  }
  
  // 尝试连接到开发者工具
  console.log('\n2. 尝试连接到开发者工具...');
  try {
    const connectOptions = {
      port: config.port,
      wsEndpoint: `ws://localhost:${config.port}`,
      closeOnExit: true
    };
    console.log('连接选项:', JSON.stringify(connectOptions, null, 2));
    
    const miniProgram = await automator.connect(connectOptions);
    console.log('✓ 成功连接到开发者工具');
    return miniProgram;
  } catch (connectError) {
    console.log('× 连接失败:', connectError.message);
    
    // 如果无法连接，则尝试启动开发者工具
    console.log('\n3. 尝试启动开发者工具...');
    try {
      const launchOptions = {
        projectPath: config.projectPath,
        cliPath: config.cliPath,
        timeout: config.timeout || 60000
      };
      console.log('启动选项:', JSON.stringify(launchOptions, null, 2));
      
      const miniProgram = await automator.launch(launchOptions);
      console.log('✓ 成功启动开发者工具');
      return miniProgram;
    } catch (launchError) {
      console.log('× 启动失败:', launchError.message);
      
      // 最后的尝试：直接使用命令行并等待更长时间
      console.log('\n4. 最后尝试：使用命令行并重试连接...');
      try {
        // 强制重启开发者工具
        console.log('4.1 尝试关闭现有开发者工具进程...');
        try {
          await execPromise('taskkill /f /im devtools.exe');
          console.log('已关闭现有开发者工具进程');
        } catch (killError) {
          console.log('没有找到运行中的开发者工具进程 (这不是错误)');
        }
        
        // 等待进程完全关闭
        await new Promise(resolve => setTimeout(resolve, 3000));
        
        // 重新打开开发者工具
        console.log('4.2 重新打开开发者工具...');
        const restartCmd = `"${config.cliPath}" open --project "${config.projectPath}"`;
        console.log('执行命令:', restartCmd);
        const { stdout: restartStdout } = await execPromise(restartCmd);
        console.log('重新打开结果:', restartStdout);
        
        // 等待更长时间让开发者工具完全启动
        console.log('等待开发者工具启动 (10秒)...');
        await new Promise(resolve => setTimeout(resolve, 10000));
        
        // 启用自动化
        console.log('4.3 重新启用自动化...');
        const autoCmd = `"${config.cliPath}" auto --project "${config.projectPath}" --auto-port ${config.port}`;
        console.log('执行命令:', autoCmd);
        await execPromise(autoCmd);
        
        // 再次等待
        console.log('等待自动化生效 (5秒)...');
        await new Promise(resolve => setTimeout(resolve, 5000));
        
        // 尝试最后一次连接
        console.log('4.4 最后一次连接尝试...');
        const finalProgram = await automator.connect({
          port: config.port,
          wsEndpoint: `ws://localhost:${config.port}`,
          closeOnExit: true
        });
        console.log('✓ 最后一次尝试成功连接到开发者工具');
        return finalProgram;
      } catch (finalError) {
        console.log('× 所有连接尝试均失败');
        throw new Error(`无法连接到微信开发者工具: ${finalError.message}`);
      }
    }
  }
}

module.exports = setupMiniProgramTest;