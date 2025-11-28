<template>
  <div class="home">
    <el-container>
      <el-header class="header">
        <div class="logo-section">
          <h1>比佳佳商城 - 智能比价平台</h1>
          <div class="tagline">全网比较 · 智能推荐 · 无需登录</div>
        </div>
      </el-header>
      <el-main>
        <div class="search-section">
          <div class="ai-assistant">
            <el-icon><MagicStick /></el-icon>
            <span>AI助手在线为您提供智能购物建议</span>
          </div>
          <el-input
            v-model="searchKeyword"
            placeholder="请输入要搜索的商品关键词"
            size="large"
            @keyup.enter="handleSearch"
          >
            <template #append>
              <el-button @click="handleSearch" type="primary">
                <el-icon><Search /></el-icon>
                智能搜索
              </el-button>
            </template>
          </el-input>
        </div>
        
        <div class="features">
          <el-row :gutter="20">
            <el-col :span="6">
              <el-card class="feature-card">
                <div class="feature-icon">
                  <el-icon><Grid /></el-icon>
                </div>
                <h3>全网比价</h3>
                <p>聚合各大电商平台商品信息，一键对比价格</p>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="feature-card">
                <div class="feature-icon">
                  <el-icon><MagicStick /></el-icon>
                </div>
                <h3>智能推荐</h3>
                <p>AI大模型分析，为您提供最佳购买建议</p>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="feature-card">
                <div class="feature-icon">
                  <el-icon><User /></el-icon>
                </div>
                <h3>无需登录</h3>
                <p>无需注册登录，直接使用所有核心功能</p>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="feature-card">
                <div class="feature-icon">
                  <el-icon><DataAnalysis /></el-icon>
                </div>
                <h3>数据分析</h3>
                <p>深度学习算法，精准预测价格趋势</p>
              </el-card>
            </el-col>
          </el-row>
        </div>
        
        <div class="ai-config-prompt" v-if="!aiConfigured">
          <el-alert
            title="提示：为了获得更好的AI推荐体验，请配置您的AI大模型API密钥"
            type="info"
            show-icon
            closable
            @close="handleConfigureAI"
          >
            <template #default>
              <div class="ai-config-content">
                <p>本系统支持多种AI大模型，包括阿里云Qwen、百度文心一言等</p>
                <el-button type="primary" @click="handleConfigureAI" size="small">立即配置</el-button>
              </div>
            </template>
          </el-alert>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { MagicStick, Search, Grid, User, DataAnalysis } from '@element-plus/icons-vue'

export default {
  name: 'HomeView',
  components: {
    MagicStick,
    Search,
    Grid,
    User,
    DataAnalysis
  },
  setup() {
    const searchKeyword = ref('')
    const router = useRouter()
    const aiConfigured = ref(false)
    
    const handleSearch = () => {
      if (searchKeyword.value.trim()) {
        router.push({
          name: 'search',
          query: { 
            keyword: searchKeyword.value
          }
        })
      }
    }
    
    const handleConfigureAI = () => {
      // 显示AI配置对话框
      aiConfigured.value = true
      // 这里可以跳转到配置页面或者显示配置对话框
      console.log('配置AI大模型API密钥')
    }
    
    return {
      searchKeyword,
      aiConfigured,
      handleSearch,
      handleConfigureAI
    }
  }
}
</script>

<style scoped lang="scss">
@import '../styles/variables.scss';

.home {
  .header {
    background: linear-gradient(135deg, $primary-color 0%, $neon-purple 100%);
    color: white;
    text-align: center;
    padding: 30px 20px;
    border-radius: 15px;
    margin: 20px;
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
    backdrop-filter: blur(10px);
    border: 1px solid $border-color-light;
    
    .logo-section {
      h1 {
        font-size: 2.5rem;
        margin: 0 0 10px 0;
        font-weight: 700;
        letter-spacing: 1px;
        text-shadow: 0 0 10px rgba(255, 255, 255, 0.5);
      }
      
      .tagline {
        font-size: 1.1rem;
        opacity: 0.9;
        letter-spacing: 2px;
      }
    }
  }
  
  .search-section {
    max-width: 700px;
    margin: 50px auto;
    padding: 20px;
    background: $card-background;
    border-radius: 15px;
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
    backdrop-filter: blur(10px);
    border: 1px solid $border-color-light;
    
    .ai-assistant {
      display: flex;
      align-items: center;
      justify-content: center;
      margin-bottom: 20px;
      color: $neon-blue;
      font-weight: 500;
      font-size: 1.1rem;
      
      .el-icon {
        margin-right: 10px;
        font-size: 1.2rem;
        animation: pulse 2s infinite;
      }
    }
  }
  
  .features {
    max-width: 1200px;
    margin: 50px auto;
    
    .feature-card {
      text-align: center;
      height: 180px;
      transition: all 0.3s ease;
      border: none;
      box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
      background: $card-background;
      border-radius: 15px;
      backdrop-filter: blur(10px);
      border: 1px solid $border-color-light;
      
      &:hover {
        transform: translateY(-5px);
        box-shadow: 0 12px 30px rgba(0, 0, 0, 0.4);
        border-color: $neon-blue;
      }
      
      .feature-icon {
        font-size: 2rem;
        color: $neon-blue;
        margin-bottom: 15px;
        text-shadow: 0 0 10px rgba(102, 126, 234, 0.5);
      }
      
      h3 {
        color: $text-primary;
        margin-bottom: 15px;
        font-size: 1.2rem;
      }
      
      p {
        color: $text-secondary;
        font-size: 0.9rem;
        line-height: 1.5;
      }
    }
  }
  
  .ai-config-prompt {
    max-width: 1200px;
    margin: 30px auto;
    
    .ai-config-content {
      display: flex;
      justify-content: space-between;
      align-items: center;
      
      p {
        margin: 10px 0;
      }
    }
  }
}

@keyframes pulse {
  0% {
    transform: scale(1);
    opacity: 1;
  }
  50% {
    transform: scale(1.1);
    opacity: 0.7;
  }
  100% {
    transform: scale(1);
    opacity: 1;
  }
}
</style>