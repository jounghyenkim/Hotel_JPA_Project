const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
  // common
  lintOnSave: false, // 오류 상황 미리 고지 옵션
  outputDir: "../src/main/resources/static",  // 빌드 타겟 디렉토리
  devServer: {
    proxy: {
      '/api': {
        // '/api' 로 들어오면 포트 8081(스프링 서버)로 proxy 처리
        target: 'http://localhost:80',
        changeOrigin: true // cross origin 허용 (서로 다른 포트 리소스 공유)
      }
    }
  },


})