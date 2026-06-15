import vue from 'vue';
import vueRouter from 'vue-router';
import ActiveList from './components/ActiveList';
import BlockedList from './components/BlockedList';
vue.use(vueRouter);

export default new vueRouter({

    routes:[
        {  path:'/',component: ActiveList},
        {  path:'/b',component: BlockedList}
    ],
    mode:'history'
});