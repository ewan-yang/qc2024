import path from 'node:path'
import { defineConfig, loadEnv } from 'vite'
import Vue from '@vitejs/plugin-vue'
import Pages from 'vite-plugin-pages'
import Components from 'unplugin-vue-components/vite'
import AutoImport from 'unplugin-auto-import/vite'
import UnoCSS from 'unocss/vite'
import VueMacros from 'unplugin-vue-macros/vite'
import pxtovw from 'postcss-px-to-viewport-8-plugin'
import { VantResolver } from 'unplugin-vue-components/resolvers'

export default defineConfig(({ mode }) => {
  console.log("🚀 ~ defineConfig ~ mode:", mode)
  const env = loadEnv(mode, process.cwd())
  return {
    base: './',
    server: {
      host: '0.0.0.0',
      proxy: {
        '/api/v1': {
          target: env.VITE_PROXY_URL,
changeOrigin: true,

        },
        '/relay-task-service/api/': {
          target: env.VITE_PROXY_URL,
changeOrigin: true,
        },
        '/analysis-alarm-service/api/': {
          target: env.VITE_PROXY_URL,
changeOrigin: true,
        },
      },
    },
    resolve: {
      alias: {
        '~/': `${path.resolve(__dirname, 'src')}/`,
      },
    },
    plugins: [
      VueMacros({
        defineOptions: false,
        defineModels: false,
        reactivityTransform: false,
        plugins: {
          vue: Vue({
            script: {
              propsDestructure: true,
              defineModel: true,
            },
          }),
        },
      }),

      // https://github.com/hannoeru/vite-plugin-pages
      Pages(),

      // https://github.com/antfu/unplugin-auto-import
      AutoImport({
        imports: [
          'vue',
          'vue-router',
          '@vueuse/core',
        ],
        ignore: ['./public/iscp/iscp-ext.js', './public/iscp/jquery-2.1.4.js'],
        dts: true,
        dirs: [
          './src/composables',
        ],
        vueTemplate: true,
      }),

      // https://github.com/antfu/vite-plugin-components
      Components({
        dts: true,
        resolvers: [VantResolver()],
      }),

      // https://github.com/antfu/unocss
      // see uno.config.ts for config
      UnoCSS(),
    ],
    css: {
      postcss: {
        plugins: [
          pxtovw({
            viewportWidth: 375,
            viewportUnit: 'vw',
            exclude: [/^(?!.*node_modules\/vant)/],
          }),
        ],
      },
    },
  }
})
