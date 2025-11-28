<template>
  <div class="product-card glass-morphism">
    <el-card :body-style="{ padding: '0px' }" class="product-card-inner">
      <div class="product-image">
        <img :src="product.imageUrl || defaultImage" alt="商品图片">
      </div>
      <div class="product-info">
        <div class="product-name neon-text">{{ product.name }}</div>
        <div class="product-price">¥{{ product.price }}</div>
        <div class="product-source">{{ product.sourceMall }}</div>
        <div class="product-metrics">
          <div class="metric">
            <span class="metric-label">销量:</span>
            <span class="metric-value">{{ product.salesVolume }}</span>
          </div>
          <div class="metric">
            <span class="metric-label">评论:</span>
            <span class="metric-value">{{ product.reviewCount }}</span>
          </div>
        </div>
        <div class="product-rating">
          <el-rate
            v-model="product.rating"
            disabled
            show-score
            score-template="{value}分"
          />
        </div>
        <div class="product-actions">
          <el-button type="primary" size="small" @click="viewProduct" class="neon-border">查看商品</el-button>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import { ref } from 'vue'

export default {
  name: 'ProductCard',
  props: {
    product: {
      type: Object,
      required: true
    }
  },
  setup(props) {
    const defaultImage = ref('https://via.placeholder.com/200x200.png?text=商品图片')
    
    const viewProduct = () => {
      if (props.product.productUrl) {
        window.open(props.product.productUrl, '_blank')
      }
    }
    
    return {
      defaultImage,
      viewProduct
    }
  }
}
</script>

<style scoped lang="scss">
@import '../styles/variables.scss';

.product-card {
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  
  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 5px 20px rgba(0, 200, 255, 0.3);
  }
  
  .product-card-inner {
    background-color: $background-color-light;
    border: none;
    border-radius: 10px;
    overflow: hidden;
  }
  
  .product-image {
    img {
      width: 100%;
      height: 200px;
      object-fit: cover;
      background-color: $background-color-dark;
    }
  }
  
  .product-info {
    padding: 14px;
    
    .product-name {
      font-size: 16px;
      font-weight: bold;
      margin-bottom: 10px;
      height: 40px;
      overflow: hidden;
      text-overflow: ellipsis;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
    }
    
    .product-price {
      color: $neon-green;
      font-size: 18px;
      font-weight: bold;
      margin-bottom: 5px;
      text-shadow: 0 0 5px rgba(0, 255, 157, 0.5);
    }
    
    .product-source {
      color: $text-secondary;
      font-size: 14px;
      margin-bottom: 10px;
    }
    
    .product-metrics {
      display: flex;
      justify-content: space-between;
      margin-bottom: 10px;
      
      .metric {
        .metric-label {
          color: $text-secondary;
          font-size: 12px;
        }
        
        .metric-value {
          color: $text-regular;
          font-size: 13px;
          font-weight: bold;
        }
      }
    }
    
    .product-rating {
      margin-bottom: 15px;
      
      :deep(.el-rate__icon) {
        color: $neon-purple;
      }
    }
    
    .product-actions {
      .el-button {
        width: 100%;
        background-color: transparent;
        border-color: $neon-blue;
        color: $neon-blue;
        
        &:hover {
          background-color: rgba(0, 200, 255, 0.1);
          box-shadow: 0 0 10px $neon-blue;
        }
      }
    }
  }
}
</style>