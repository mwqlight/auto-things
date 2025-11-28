<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<style lang="scss">
@import './styles/variables.scss';

#app {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: $text-primary;
  background-color: $background-color-base;
  min-height: 100vh;
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

// 全局科技感样式
.neon-text {
  color: $neon-blue;
  text-shadow: 0 0 5px #fff, 0 0 10px #fff, 0 0 15px #00c8ff, 0 0 20px #00c8ff;
}

.neon-border {
  border: 1px solid $neon-blue;
  box-shadow: 0 0 5px $neon-blue, 0 0 10px $neon-blue;
}

.glass-morphism {
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(10px);
  border-radius: 10px;
  border: 1px solid rgba(255, 255, 255, 0.1);
}
</style>