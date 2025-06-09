<script setup>
import { listTags, createTag, deleteTag, updateTag } from '@/api/tags'
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const tags = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const form = ref({ id: null, name: '', description: '', color: '' })
const isEdit = ref(false)
const fetchTags = () => {
    loading.value = true
    listTags().then(res => {
        tags.value = res.data
    }).finally(() => {
        loading.value = false
    })
}

onMounted(() => {
    fetchTags()
})

const handleAdd = () => {
    dialogTitle.value = '新增标签'
    isEdit.value = false
    form.value = { id: null, name: '', description: '', color: '' }
    dialogVisible.value = true
}

const handleEdit = (row) => {
    dialogTitle.value = '编辑标签'
    isEdit.value = true
    form.value = { ...row }
    dialogVisible.value = true
}

const handleDelete = (row) => {
    ElMessageBox.confirm('确定要删除该标签吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
    }).then(() => {
        deleteTag(row.id).then(() => {
            ElMessage.success('删除成功')
            fetchTags()
        })
    }).catch(() => {})
}

const handleSubmit = () => {
    if (!form.value.name) {
        ElMessage.warning('请输入标签名称')
        return
    }
    if (isEdit.value) {
        updateTag(form.value.id, {
            name: form.value.name,
            description: form.value.description,
            color: form.value.color
        }).then(() => {
            ElMessage.success('修改成功')
            dialogVisible.value = false
            fetchTags()
        })
    } else {
        createTag({
            name: form.value.name,
            description: form.value.description,
            color: form.value.color
        }).then(() => {
            ElMessage.success('新增成功')
            dialogVisible.value = false
            fetchTags()
        })
    }
}
</script>

<template>
    
<el-card>
    <div style="margin-bottom: 16px; text-align: right;">
        <el-button type="primary" @click="handleAdd">新增标签</el-button>
    </div>
    <el-table :data="tags" style="width: 100%" :loading="loading">
        <el-table-column label="标签名称">
            <template #default="scope">
                <el-tag :style="{ backgroundColor: scope.row.color, color: '#fff', border: 'none', cursor: 'pointer', transition: 'background 0.2s' }">
                    {{ scope.row.name }}
                </el-tag>
            </template>
        </el-table-column>
        <el-table-column prop="description" label="描述" />
        <el-table-column label="操作" width="180">
            <template #default="scope">
                <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
                <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
            </template>
        </el-table-column>
    </el-table>
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="400px">
        <el-form :model="form" label-width="80px">
            <el-form-item label="标签名称">
                <el-input v-model="form.name" />
            </el-form-item>
            <el-form-item label="描述">
                <el-input v-model="form.description" />
            </el-form-item>
            <el-form-item label="颜色">
                <el-color-picker v-model="form.color" />
                <el-input v-model="form.color" style="width: 120px; margin-left: 10px;" />
            </el-form-item>
        </el-form>
        <template #footer>
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary" @click="handleSubmit">确定</el-button>
        </template>
    </el-dialog>
</el-card>
</template>

<style scoped>
/* 移除悬停效果，保持 el-tag 悬停和普通状态一致 */
:deep(.el-tag) {
  transition: none;
}
</style>

