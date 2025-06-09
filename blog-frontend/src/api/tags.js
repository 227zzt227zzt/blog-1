import request from '@/utils/request'

export function listTags() {
    return request.get('/tags');
  }

export function createTag(data) {
    return request.post('/tags', data);
  }

export function deleteTag(id) {
    return request.delete(`/tags/${id}`);
}

export function updateTag(id, data) {
    return request.put(`/tags/${id}` , data);
}
  
  
