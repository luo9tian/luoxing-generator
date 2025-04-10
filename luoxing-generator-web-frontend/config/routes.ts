export default [
  {
    path: '/user',
    layout: false,
    routes: [
      { path: '/user/login', component: './User/Login' },
      { path: '/user/register', component: './User/Register' },
    ],
  },
  { path: '/home', icon: 'home', component: './Index/Welcome', name: '主页' },
  { path: '/generator/add', icon: 'plus', component: './Generator/Add', name:"创建生成器" },
  {
    path: '/generator/update',
    icon: 'plus',
    component: './Generator/Add',
    name: '修改生成器',
    hideInMenu: true,
  },
  {
    path: '/generator/detail/:id',
    icon: 'plus',
    component: './Generator/Detail',
    name: '生成器详情',
    hideInMenu: true,
  },
  { path: '/test', icon: 'home', component: './Test/Index', name: '测试上传下载' },
  {
    path: '/admin',
    icon: 'crown',
    name: '管理页',
    access: 'canAdmin',
    routes: [
      { path: '/admin', redirect: '/admin/user' },
      { icon: 'table', path: '/admin/user', component: './Admin/User', name: '用户管理' },
      { icon: 'table', path: '/admin/generator', component: './Admin/Generator', name: '生成器管理' },
    ],
  },
  { path: '/', redirect: '/home' },
  { path: '*', layout: false, component: './404' },
];
