<template>
  <div class="search">
    <el-container>
      <el-header class="header">
        <el-page-header @back="goBack" title="返回首页">
          <template #content>
            <div class="header-content">
              <span>商品搜索结果</span>
              <div class="ai-status" v-if="!aiApiKeyConfigured">
                <el-tag type="warning" @click="handleConfigureAI">AI推荐未配置</el-tag>
              </div>
            </div>
          </template>
        </el-page-header>
      </el-header>
      <el-main>
        <div class="search-info" v-if="keyword">
          <h2> "{{ keyword }}" 的搜索结果</h2>
          <div class="result-summary">
            共找到 {{ products.length }} 个相关商品
          </div>
        </div>
        
        <div class="loading" v-if="loading">
          <el-skeleton animated />
          <div class="loading-text">正在从各大电商平台抓取最新商品信息...</div>
        </div>
        
        <div class="ai-config-prompt" v-if="!aiApiKeyConfigured && !loading">
          <el-alert
            title="提示：为了获得更好的AI推荐体验，请配置您的AI大模型API密钥"
            type="info"
            show-icon
            closable
          >
            <template #default>
              <div class="ai-config-content">
                <p>本系统支持多种AI大模型，包括阿里云Qwen、百度文心一言等</p>
                <el-button type="primary" @click="handleConfigureAI" size="small">立即配置</el-button>
              </div>
            </template>
          </el-alert>
        </div>
        
        <div class="recommendation" v-if="recommendation && aiApiKeyConfigured">
          <el-alert
            :title="recommendation"
            type="success"
            show-icon
            :closable="false"
          />
        </div>
        
        <div class="products" v-if="!loading && products.length > 0">
          <el-table :data="products" style="width: 100%" stripe>
            <el-table-column prop="name" label="商品名称" min-width="200" />
            <el-table-column prop="sourceMall" label="来源商城" width="120" />
            <el-table-column prop="price" label="价格" width="120">
              <template #default="scope">
                ¥{{ scope.row.price }}
              </template>
            </el-table-column>
            <el-table-column prop="brand" label="品牌" width="120" />
            <el-table-column prop="rating" label="评分" width="100" />
            <el-table-column prop="salesVolume" label="销量" width="100" />
            <el-table-column label="操作" width="120">
              <template #default="scope">
                <el-button size="small" type="primary" @click="viewProduct(scope.row)">查看</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
        
        <div class="no-results" v-if="!loading && products.length === 0 && keyword">
          <el-empty description="暂无相关商品信息">
            <template #extra>
              <div class="empty-actions">
                <el-button type="primary" @click="goBack">返回首页</el-button>
                <el-button @click="handleConfigureAI" v-if="!aiApiKeyConfigured">配置AI推荐</el-button>
              </div>
            </template>
          </el-empty>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { searchProducts } from '@/utils/api'

export default {
  name: 'SearchView',
  setup() {
    const route = useRoute()
    const router = useRouter()
    
    const keyword = ref('')
    const loading = ref(false)
    const products = ref([])
    const recommendation = ref('')
    const aiApiKeyConfigured = ref(true) // 默认认为已配置
    
    const goBack = () => {
      router.push({ name: 'home' })
    }
    
    const viewProduct = (product) => {
      if (product.productUrl) {
        window.open(product.productUrl, '_blank')
      }
    }
    
    const searchProductsAPI = async () => {
      if (!keyword.value) return
      
      loading.value = true
      try {
        // 调用真实的后端API
        const response = await searchProducts(keyword.value)
        products.value = response.products || []
        recommendation.value = response.recommendation || ''
      } catch (error) {
        console.error('搜索失败:', error)
        // 错误处理
        products.value = []
        recommendation.value = '搜索过程中出现错误，请稍后重试'
      } finally {
        loading.value = false
      }
    }
    
    const handleConfigureAI = () => {
      // 显示AI配置对话框
      console.log('配置AI大模型API密钥')
      // 这里可以跳转到配置页面或者显示配置对话框
    }
    
    onMounted(() => {
      keyword.value = route.query.keyword || ''
      if (keyword.value) {
        searchProductsAPI()
      }
    })
    
    return {
      keyword,
      loading,
      products,
      recommendation,
      aiApiKeyConfigured,
      goBack,
      viewProduct,
      handleConfigureAI
    }
  }
}
</script>

<style scoped lang="scss">
@import '../styles/variables.scss';

.search {
  height: 100%;
  background: $background-color-base;
  min-height: 100vh;
  padding: 20px;
}

.header {
  background: linear-gradient(135deg, $primary-color 0%, $neon-purple 100%);
  border-bottom: 1px solid $border-color-light;
  padding: 10px 20px;
  color: white;
  border-radius: 15px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
  backdrop-filter: blur(10px);
  border: 1px solid $border-color-light;
  margin-bottom: 20px;
}

.header :deep(.el-page-header__title) {
  color: rgba(255, 255, 255, 0.8);
}

.header :deep(.el-page-header__content) {
  color: white;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.ai-status {
  cursor: pointer;
}

.ai-status :deep(.el-tag) {
  background: rgba(255, 255, 255, 0.2);
  border-color: rgba(255, 255, 255, 0.3);
  color: white;
}

.search-info h2 {
  color: $text-primary;
  margin-bottom: 10px;
  font-size: 1.8rem;
}

.result-summary {
  color: $text-secondary;
  font-size: 14px;
  margin-bottom: 20px;
}

.loading {
  margin-top: 20px;
  text-align: center;
  background: $card-background;
  border-radius: 15px;
  padding: 30px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
  backdrop-filter: blur(10px);
  border: 1px solid $border-color-light;
}

.loading-text {
  margin-top: 10px;
  color: $text-secondary;
  font-size: 14px;
}

.recommendation {
  margin-bottom: 20px;
}

.products {
  background: $card-background;
  border-radius: 15px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
  padding: 20px;
  backdrop-filter: blur(10px);
  border: 1px solid $border-color-light;
}

.no-results {
  text-align: center;
  margin-top: 50px;
  background: $card-background;
  border-radius: 15px;
  padding: 30px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
  backdrop-filter: blur(10px);
  border: 1px solid $border-color-light;
}

.empty-actions {
  margin-top: 20px;
}

.empty-actions .el-button {
  margin: 0 10px;
}

.ai-config-prompt {
  margin-bottom: 20px;
  background: $card-background;
  border-radius: 15px;
  padding: 20px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
  backdrop-filter: blur(10px);
  border: 1px solid $border-color-light;
}

.ai-config-prompt :deep(.el-alert__content) {
  width: 100%;
}

.ai-config-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 10px;
}

.ai-config-content p {
  margin: 0;
  flex: 1;
  text-align: left;
  color: $text-primary;
}

@media (max-width: 768px) {
  .ai-config-content {
    flex-direction: column;
  }
  
  .ai-config-content p {
    text-align: center;
    margin-bottom: 10px;
  }
}
</style>