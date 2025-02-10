import { createApp } from 'vue';
import { setupI18n } from '@/locales/setupI18n';
import App from './App.vue';
import AppLoading from './components/common/AppLoading.vue';
import { setupDirectives } from './directives';
import { setupRouter } from './router';
import { setupAssets, setupNaive } from './plugins';
import { setupStore } from './store';

async function setupApp() {
  // import assets: js、css
  setupAssets();

  // app loading
  const appLoading = createApp(AppLoading);

  appLoading.mount('#appLoading');

  const app = createApp(App);

  // store plugin: pinia
  setupStore(app);

  // vue custom directives
  setupDirectives(app);
  await setupI18n(app);
  // vue router
  await setupRouter(app);
  setupNaive(app);

  // mount app
  app.mount('#app');
}

setupApp();
