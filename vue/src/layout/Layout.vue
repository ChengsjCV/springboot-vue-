<template>
  <!-- 后台主页的框架页面 -->
  <div>
    <!-- 头部 -->
    <navHeader></navHeader>
    <!-- 主体 -->
    <div style="display: flex">
      <!-- 侧边栏 -->
      <Aside></Aside>
      <!-- 内容区域 -->
      <router-view style="flex : 1"/>
    </div>
  </div>
</template>

<script>
import navHeader from '@/components/navHeader'
import Aside from '@/components/Aside'
export default {
    name: 'Layout',
    components: {
      navHeader,
      Aside
    }
}
</script>

<style>

</style>