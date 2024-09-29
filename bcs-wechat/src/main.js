import Vue from 'vue';
import App from './App';
import { router } from './router';
import Vant from 'vant';
import 'vant/lib/index.css';
import 'bootstrap/dist/css/bootstrap.css'
import './assets/tailwind.css'; // Add this line to include Tailwind
import './assets/normalize.css'; // Add this line to include Tailwind
import request from '@/api/request'
import TreeNode from "@/components/TreeNode/index.vue";

Vue.use(Vant);
Vue.prototype.$request = request;
Vue.component('TreeNode', TreeNode)
new Vue({
  router,
  el: '#app',
  render: h => h(App)
});
