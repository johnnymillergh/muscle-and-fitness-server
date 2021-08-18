// 64 megabytes
const BYTES_PER_CHUNK = 64 * 1048576
// noinspection JSUnresolvedFunction
new Vue({
    el: '#app',
    data: {
        appInfo: null,
        videoSource: '',
        split: window.location.href.split('/')
    },
    methods: {
        async onFilePicked(event) {
            const files = event.target.files
            const blob = files[0]
            console.info('blob', blob)
            let startByte = 0
            let endByte = 0
            let index = 0
            const slices = Math.ceil(blob.size / BYTES_PER_CHUNK)
            const uploadResourceChunkPayload = {
                bucket: '',
                filename: blob.name
            }
            const objectList = []
            while (startByte < blob.size) {
                endByte = startByte + BYTES_PER_CHUNK
                if (endByte > blob.size) {
                    endByte = blob.size
                }
                const response = await this.uploadResourceChunk(blob, index, startByte, endByte, uploadResourceChunkPayload)
                objectList.push(response.data.object)
                console.info('response', response)
                if (index === 0) {
                    uploadResourceChunkPayload.bucket = response.data.bucket
                    console.warn('Set uploadResourceChunkPayload', uploadResourceChunkPayload)
                }
                startByte = endByte
                index++
                if (index >= slices) {
                    if (slices === 1) {
                        console.error(`The slices count is ${slices}, should not be Uploaded by chunk`)
                    }
                    console.info(`Finished uploading all chunks. index >= slices. index = ${index}, slice = ${slices}`);
                    const mergeResponse = await this.mergeResourceChunk(uploadResourceChunkPayload.bucket, objectList);
                    console.info('Finished merging all chunks', mergeResponse);
                }
            }
        },
        async uploadResourceChunk(blob, index, start, end, uploadResourceChunkPayload) {
            console.info(`uploadFile blog: ${blob}, index: ${index}, start: ${start}, end: ${end}, uploadResourceChunkPayload: `, uploadResourceChunkPayload)
            const formData = new FormData()
            formData.append('bucket', uploadResourceChunkPayload.bucket)
            formData.append('chunkNumber', index)
            formData.append('filename', uploadResourceChunkPayload.filename)
            formData.append('file', blob.slice(start, end))
            const url = `${this.split[0]}//${this.split[2]}/upload/chunk`
            const resp = await fetch(url, {
                method: 'POST',
                body: formData,
                headers: {
                    'Content-Disposition': `form-data; name="file"; filename="${blob.name}"`
                }
            })
            return await resp.json()
        },
        async mergeResourceChunk(bucket, objectList) {
            const mergeResourcePayload = {
                'bucket': bucket,
                'objectList': objectList
            }
            const url = `${this.split[0]}//${this.split[2]}/merge/chunk`
            const resp = await fetch(url, {
                method: 'PUT',
                body: JSON.stringify(mergeResourcePayload),
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            return await resp.json()
        }
    },
    mounted() {
        console.log('mounted')
    }
});
