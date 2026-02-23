/**
 * 共通JavaScript - Bug知識庫
 */

// コンソール出力用
function logToConsole(message, type = 'info') {
    const console = document.getElementById('console');
    if (!console) return;
    
    const timestamp = new Date().toLocaleTimeString();
    const prefix = type === 'error' ? 'ERROR' : type === 'success' ? 'SUCCESS' : 'INFO';
    const color = type === 'error' ? '#f85149' : type === 'success' ? '#3fb950' : '#58a6ff';
    
    console.innerHTML += `<div style="color:${color}">[${timestamp}] ${prefix}: ${message}</div>`;
    console.scrollTop = console.scrollHeight;
}

// バグ実行シミュレーション
function runBuggy() {
    logToConsole('Executing buggy code...', 'error');
    setTimeout(() => {
        logToConsole('Exception: NoSuchBeanDefinitionException', 'error');
    }, 500);
}

// 修正版実行シミュレーション
function runFixed() {
    logToConsole('Executing fixed code...', 'info');
    setTimeout(() => {
        logToConsole('Success: Bean injected correctly', 'success');
    }, 500);
}

// ページ読み込み時の初期化
document.addEventListener('DOMContentLoaded', function() {
    // バグ実行ボタン
    const bugBtn = document.querySelector('.btn-bug');
    if (bugBtn) {
        bugBtn.addEventListener('click', runBuggy);
    }
    
    // 修正版実行ボタン
    const fixBtn = document.querySelector('.btn-fix');
    if (fixBtn) {
        fixBtn.addEventListener('click', runFixed);
    }
});
