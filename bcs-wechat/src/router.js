import Vue from 'vue';
import Router from 'vue-router';
import {getToken} from "@/libs/util";

Vue.use(Router);

const routes = [
  {
    path: '*',
    redirect: '/user'
  },
    {
        name: 'login',
        component: () => import('./view/login'),
        meta: {
            title: '业务佣金系统'
        }
    },
  {
    name: 'user',
    component: () => import('./view/user'),
    meta: {
      title: '业务佣金系统',
        keepAlive: true,
        isBack: false
    }
  },
    {
        name: 'yjrecord',
        component: () => import('./view/yonngjin/index.vue'),
        meta: {
            title: '业务佣金系统'
        }
    },
    {
        name: 'yjtx',
        component: () => import('./view/yonngjin/tx.vue'),
        meta: {
            title: '业务佣金系统'
        }
    },
    {
        name: 'yjtixian',
        component: () => import('./view/yonngjin/tixian.vue'),
        meta: {
            title: '业务佣金系统'
        }
    },
    {
        name: 'hfrecord',
        component: () => import('./view/huafei/index.vue'),
        meta: {
            title: '业务佣金系统'
        }
    },
    {
        name: 'hftx',
        component: () => import('./view/huafei/tx.vue'),
        meta: {
            title: '业务佣金系统'
        }
    },
    {
        name: 'hftixian',
        component: () => import('./view/huafei/tixian.vue'),
        meta: {
            title: '业务佣金系统'
        }
    },
    {
        name: 'team',
        component: () => import('./view/team'),
        meta: {
            title: '业务佣金系统'
        }
    },
  {
    name: 'cart',
    component: () => import('./view/cart'),
    meta: {
      title: '购物车'
    }
  },
    {
        name: 'apply',
        component: () => import('./view/apply'),
        meta: {
            title: '业务佣金系统',
            requiresAuth: "true"
        }
    },
    {
        name: 'question',
        component: () => import('./view/question'),
        meta: {
            title: '业务佣金系统',
        }
  }
];

// add route path
routes.forEach(route => {
  route.path = route.path || '/' + (route.name || '');
});

const router = new Router({ routes });

router.beforeEach((to, from, next) => {
  // const title = to.meta && to.meta.title;
  // if (title) {
  //   document.title = title;
  // }
  //
  //   const isAuthenticated = !!getToken(); // 假设通过 token 判断是否登录
  //   if (to.meta.requiresAuth && !isAuthenticated) {
  //       next({
  //           path: '/login',
  //           query: { redirect: to.fullPath } // 保存用户原来的路径
  //       });
  //   } else {
        next();
    // }
});

export {
  router
};
