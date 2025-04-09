import FileUploader from '@/components/FileUploader';
import PictureUploader from '@/components/PictureUploader';
import { COS_HOST } from '@/constants';
import {
  addGeneratorUsingPost, downloadGeneratorByIdUsingGet,
  editGeneratorUsingPost,
  getGeneratorVoByIdUsingGet,
} from '@/services/backend/generatorController';
import {Link, useParams, useSearchParams} from '@@/exports';
import type { ProFormInstance } from '@ant-design/pro-components';
import {
  PageContainer,
  ProCard,
  ProFormSelect,
  ProFormText,
  ProFormTextArea,
  StepsForm,
} from '@ant-design/pro-components';
import { ProFormItem } from '@ant-design/pro-form';
import { history } from '@umijs/max';
import {Button, message, Space, Tabs, TabsProps, Tag, UploadFile} from 'antd';
import React, { useEffect, useRef, useState } from 'react';
import FileConfig from "@/pages/Generator/Detail/component/FileConfig";
import ModelConfig from "@/pages/Generator/Detail/component/ModelConfig";
import AuthorInfo from "@/pages/Generator/Detail/component/AuthorInfo";
import {DownloadOutlined, EditOutlined} from "@ant-design/icons";
import {testDownloadUsingGet} from "@/services/backend/fileController";
import {useModel} from "@@/plugin-model";

/**
 * 创建生成器页面
 * @constructor
 */
const GeneratorDetailPage: React.FC = () => {
  const {id}=useParams();
  const [loading,setLoading]=useState<boolean>(true)
  const [data,setData]=useState<API.GeneratorVO>({})
  const {initialState}=useModel('@@initialState');
  const {currentUser}=initialState??{}
  const my=currentUser?.id===data.userId
  /**
   * 加载数据
   */
  const loadData = async () => {
    if (!id) {
      return;
    }
    setLoading(true);
    try {
      const res = await getGeneratorVoByIdUsingGet({
        id
      });

        setData(res.data??{})
        console.log(res.data);
    } catch (error: any) {
      message.error('加载数据失败，' + error.message);
    }
    setLoading(false)
  };
  const onChange = (key: string) => {
    console.log(key);
  };
  const downloadButton = data.distPath && currentUser && (
    <Button icon={<DownloadOutlined/>} onClick={async () => {
      const blob = await downloadGeneratorByIdUsingGet({
        id:data.id
      }, {
        responseType: "blob"
      })
      const curBlob = new Blob(blob.data);
      const dink = document.createElement("a");
      const fulTlPath = COS_HOST + data.distPath;
      dink.download = fulTlPath.substring(fulTlPath.lastIndexOf("/") + 1);
      dink.style.display = "none";
      dink.href = URL.createObjectURL(blob);
      document.body.appendChild(dink)
      dink.click();
      URL.revokeObjectURL(dink.href) // 释放URL 对象
      document.body.removeChild(dink)
    }}>下载</Button>
  )

  /**
   *编辑按钮
   */
  const editButton = my && (
    <Link to={`/generator/update?id=${data.id}`}>
      <Button icon={<EditOutlined/>}>编辑 </Button>
    </Link>
  )
  const items: TabsProps['items'] = [
    {
      key: '1',
      label: '文件配置',
      children: <FileConfig data={data}/>
    },
    {
      key: '2',
      label: '模型配置',
      children: <ModelConfig data={data}/>,
    },
    {
      key: '3',
      label: '作者信息',
      children: <AuthorInfo data={data}/>,
    },
  ];
  useEffect(() => {
    if (id) {
      loadData();
    }
  }, [id]);

  return <PageContainer title={<></>} loading={loading}>
    <ProCard
      style={{ width:"100%" }}
      boxShadow
    >
      <div slot="title">
       <span style={{fontSize:22,fontWeight:600,marginRight:10}}>
         {data?.name}
      </span>
        {
          data?.tags?.map((item,index)=>{
            return <Tag key={item} style={{fontSize:14}}>{item}</Tag>
          })
        }
      </div>
      <div style={{display:"grid",gridTemplateColumns:"1fr 1fr"}}>
        <div>
          <div style={{color:"black",margin:"5px 0px",fontSize:18}}>{data?.description}</div>
          <div style={{color:"#a7a7a7",margin:"5px 0px",fontSize:16}}>
            <span style={{display:"block",margin:"5px 0"}}>创建时间: {data?.createTime}</span>
            <span style={{display:"block",margin:"5px 0"}}>基础包: {data?.basePackage}</span>
            <span style={{display:"block",margin:"5px 0"}}>版本: {data?.version}</span>
            <span style={{display:"block",margin:"5px 0"}}>作者: {data?.author}</span>
          </div>
        </div>
        <div style={{    display: "flex", position:"relative", left: "-10%",justifyContent: "flex-end"}}>
          <img src={data?.picture} style={{width:"150px",height:"150px",borderRadius:"16px"}}></img>
        </div>
      </div>
      <Space style={{margin:"10px 0px"}}>
        <Button type={"primary"}>
          立即使用
        </Button>
        {downloadButton}
        {editButton}
      </Space>
    </ProCard>
    <ProCard
      style={{ width:"100%" ,margin:"25px 0"}}
      boxShadow
    >
      <Tabs defaultActiveKey="1" items={items} onChange={onChange} />
    </ProCard>
  </PageContainer>
};

export default GeneratorDetailPage;
