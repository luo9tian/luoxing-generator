import React, {useState} from 'react';
import {DownloadOutlined, InboxOutlined} from '@ant-design/icons';
import type { UploadProps } from 'antd';
import {Button, Flex, message, Upload} from 'antd';
import {PageContainer} from "@ant-design/pro-components";
import { Card, Col, Row } from 'antd';
import {testDownloadUsingGet, testUploadFileUsingPost} from "@/services/backend/fileController";
import {COS_HOST} from "@/constants";
const { Dragger } = Upload;

/**
 * 上传下载页
 * @constructor
 */
const IndexPage: React.FC = () => {
  const [value,setValue]=useState<string>();
  const props: UploadProps = {
    name: 'file',
    multiple: false,
    maxCount:1,
    customRequest:async (fileObj:any)=>{
      try{
        const res=await testUploadFileUsingPost({},fileObj.file);
        setValue(res.data);
        fileObj.onSuccess(res.data);
      }catch (e:any){
        message.error("上传失败"+e.message);
        fileObj.onError(e)
      }
    },
    onRemove(){
      setValue(undefined);
    }
  };
  return (
    <PageContainer title={<></>}>
      <Flex justify="center">
        <Card title="上传" variant="borderless" style={{flexShrink:1,width: "40%",marginRight:"2%"}}>
          <Dragger {...props} >
            <p className="ant-upload-drag-icon">
              <InboxOutlined/>
            </p>
            <p className="ant-upload-text">在这个区域点击或拖拽上传</p>
            <p className="ant-upload-hint">
              严禁上传违法图片.
            </p>
          </Dragger>
        </Card >
        <Card title="下载" variant="borderless" style={{flexShrink:1,width: "40%"}}>
          <p>文件上传地址：{COS_HOST+value}</p>
          <img src={COS_HOST+value}></img>
          <Button icon={<DownloadOutlined />} onClick={
            async ()=>{
              const blob=await testDownloadUsingGet({
                filePath:value},{
                responseType:"blob"
              })
              const curBlob=new Blob(blob.data);
              const dink=document.createElement("a");
              const fulTlPath=COS_HOST+value;
              dink.download=fulTlPath.substring(fulTlPath.lastIndexOf("/")+1);
              dink.style.display="none";
              dink.href=URL.createObjectURL(blob);
              document.body.appendChild(dink)
              dink.click();
              URL.revokeObjectURL(dink.href) // 释放URL 对象
              document.body.removeChild(dink)
            }
          }>Upload Directory</Button>        </Card>
      </Flex>
    </PageContainer>
  );
};
export default IndexPage;
