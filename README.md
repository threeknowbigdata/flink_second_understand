## 前言

大家好，我是土哥。

为了帮助更多从事大数据行业的小伙伴，能快速学会 Flink 组件，并且找到一份满意的大数据工作，在这里我将自己所写的 Flink 实战代码、Flink 面经、Flink 系列博文、大数据系列面经整理出来发送给大家！

麻烦走过路过的的各位朋友们给这个项目点个 star，真的写了好久，大家的 Star 就是对我坚持下来最大的一种鼓励！我会一直持续更新这个项目的。

![](https://files.mdnice.com/user/19005/a4fa823a-6ac5-47e8-a01d-24f4ccf9c994.png)

## FlinkStudy

FlinkStudy 实战代码根据 Flink 实战文档所编写，使用 Flink1.14.0 版本进行代码编程，通过 Java 和 Scala 两种代码进行教学，全文分为 11 章节，目录如下：


![](https://files.mdnice.com/user/19005/80ef0685-a70a-492c-b5d0-bb847eeda49e.png)

## Flink 系列博文


欢迎大家添加我的微信：**youzhiqiangshou_02**,获取PDF文档解压密码，同时拉你进大数据技术交流群！

<img src="https://files.mdnice.com/user/19005/b13cfefa-1229-455b-a1c1-f2a3c6207ce8.png" alt="img" style="zoom:80%;" />


扫码关注微信公众号：**3分钟秒懂大数据**，获取更多大数据、流计算博文信息。


![](https://files.mdnice.com/user/19005/9b74646c-5950-4a72-ba53-f3755c6ed667.png)

部分 Flink 文章链接如下：

[Flink 1.14.0 内存优化你不懂？跟着土哥走就对了（万字长文+参数调优）](https://mp.weixin.qq.com/s?__biz=Mzg5NDY3NzIwMA==&mid=2247499997&idx=1&sn=5037221dff9b28e86e4c6b6a4cfc968f&chksm=c0197962f76ef074ee92df03886bfb9bcd894947eaa7a1ab51086d98210e9be047fbd9647f94&token=299008056&lang=zh_CN#rd)

[一口气搞懂「Flink Metrics」监控指标和性能优化，全靠这33张图和7千字（建议收藏）](https://mp.weixin.qq.com/s?__biz=Mzg5NDY3NzIwMA==&mid=2247499736&idx=1&sn=652f3d94182d56a795a55c495c89ab2b&chksm=c0194667f76ecf71bc1e8543222effd2a9ff940b48e4c14e52a966763ddb074d8f2305bca6ac&token=299008056&lang=zh_CN#rd)

[33张图解flink sql应用提交（建议收藏！）](https://mp.weixin.qq.com/s?__biz=Mzg5NDY3NzIwMA==&mid=2247499065&idx=1&sn=65559142ae6210e937baa8e9f8a1089b&chksm=c0194486f76ecd9028e0113a34b2e633d406ef342eab676043fb159b52f4c10eaefd55a2757b&token=299008056&lang=zh_CN#rd)

[Flink 1.13.2 集群安装部署的3种方式(建议收藏)](https://mp.weixin.qq.com/s?__biz=Mzg5NDY3NzIwMA==&mid=2247498571&idx=1&sn=d4557dfb7dde1964bf0ba684c9b2ff0a&chksm=c01942f4f76ecbe2feb3504b166f94fd0762efffe7acaa804bac27dba6c17556d1b623ee8e39&token=299008056&lang=zh_CN#rd)

[重磅！ | Flink1.14新特性预览](https://mp.weixin.qq.com/s?__biz=Mzg5NDY3NzIwMA==&mid=2247497696&idx=1&sn=db2cb7921c10a378f3e15d28527ca5bf&chksm=c0194e5ff76ec749ed96a5ef361550e1d1f66c97ba571f18ef255375912fb57fdb4cfb207756&token=299008056&lang=zh_CN#rd)

[实时数仓 之 Kafka-Flink-Hive集成原理和实战代码（原理+实战）](https://mp.weixin.qq.com/s?__biz=Mzg5NDY3NzIwMA==&mid=2247497251&idx=1&sn=e6b87fe99c916bb38a4c50f3dc3fc538&chksm=c0194f9cf76ec68a8434ddf302f053ade6b1cbb6aabc865bbbc6f92398756f239a41d90d0251&token=299008056&lang=zh_CN#rd)

[史上最全干货！Flink面试大全总结（全文6万字、110个知识点、160张图）](https://mp.weixin.qq.com/s?__biz=Mzg5NDY3NzIwMA==&mid=2247497240&idx=1&sn=954c0702a2d842f9facb4e36c8c44563&chksm=c0194fa7f76ec6b1f8b41e96ca6347b0e0da7fea3077cbed02ed862a0f3e335289eda3153924&token=299008056&lang=zh_CN#rd)

[Flink 1.13 SQL最易考察知识点公布！快来看看有哪些！](https://mp.weixin.qq.com/s?__biz=Mzg5NDY3NzIwMA==&mid=2247497239&idx=1&sn=826157c19070ab5c6ce20e533a60120a&chksm=c0194fa8f76ec6be2b9efe537e9bde765b070ee01196608c44b6d6d53a14237ef34af2011d3e&token=299008056&lang=zh_CN#rd)

[搞懂Flink Checkpoint机制，实现故障恢复、应用容错能力！](https://mp.weixin.qq.com/s?__biz=Mzg5NDY3NzIwMA==&mid=2247497235&idx=1&sn=32c372a4eef36b3c1936e7dce117a075&chksm=c0194facf76ec6babd669be0f8e2b0dede7f6a2dccbb11b1c387d8a1ce172565746a0b8a8a7e&token=299008056&lang=zh_CN#rd)

[硬核！10分钟解读Flink 状态存储原理(2)](https://mp.weixin.qq.com/s?__biz=Mzg5NDY3NzIwMA==&mid=2247497227&idx=1&sn=1b48a869d12bd0622de7a1a893df816c&chksm=c0194fb4f76ec6a24aefd8030fd679c3f77cf6129ed9370123876adec6ba392e16f92ab24d47&token=299008056&lang=zh_CN#rd)

[硬核！10分钟解读Flink 状态原理(1)](https://mp.weixin.qq.com/s?__biz=Mzg5NDY3NzIwMA==&mid=2247497226&idx=1&sn=f44ecbd2ef652146a68ef7049db1f387&chksm=c0194fb5f76ec6a307b0f939f5d3be5da8b72bf091999cf0de6a034a0f7b53d629412083c649&token=299008056&lang=zh_CN#rd)

[11张图，拿下Flink端到端严格一次Exactly-Once](https://mp.weixin.qq.com/s?__biz=Mzg5NDY3NzIwMA==&mid=2247497224&idx=1&sn=8654d025accb3e2bbc6978f1801d3b66&chksm=c0194fb7f76ec6a1cd7b8cb007bec4c2f05c2e15155d238c48eac9d3c893ff921df895ade082&token=299008056&lang=zh_CN#rd)

[Flink分区策略：你可以不会，但不能不懂](https://mp.weixin.qq.com/s?__biz=Mzg5NDY3NzIwMA==&mid=2247497223&idx=1&sn=34f8c3693111afa28dd598b270ac8809&chksm=c0194fb8f76ec6aef4befeee260aa8d5cafa8e666c5ce574a6d93cbc01b8f45730cf9e2947c4&token=299008056&lang=zh_CN#rd)

[Flink 四大基石之—Window基础知识点介绍](https://mp.weixin.qq.com/s?__biz=Mzg5NDY3NzIwMA==&mid=2247497204&idx=1&sn=ade24641ffd55410a89dfe1030408039&chksm=c0194c4bf76ec55d0730e7415c47e9bc50f86da4d17a589e457b89b10100785036f17f44ea66&token=299008056&lang=zh_CN#rd)

[Flink 四大基石之时间和水位线原理介绍！](https://mp.weixin.qq.com/s?__biz=Mzg5NDY3NzIwMA==&mid=2247497202&idx=1&sn=0ab6a5008cdcc8d03c41ab517b7f7feb&chksm=c0194c4df76ec55b70a15c3235e12fb3ba7b9dc502eb34e894a78a5595ef4a073382a66e9210&token=299008056&lang=zh_CN#rd)

[Flink SQL 1.13—CDC 实践与一致性分析](https://mp.weixin.qq.com/s?__biz=Mzg5NDY3NzIwMA==&mid=2247497200&idx=1&sn=30acc078e9f30abdd8221e4ba3b508cd&chksm=c0194c4ff76ec5597168b59d5a992c4ee07c170def23e57cc144565d032fa136178c71f2b45f&token=299008056&lang=zh_CN#rd)

[Flink SQL CDC + JDBC Sink Connector 如何保证一致性！](https://mp.weixin.qq.com/s?__biz=Mzg5NDY3NzIwMA==&mid=2247497200&idx=2&sn=ae9734975d6e0318b71a0982de039951&chksm=c0194c4ff76ec55921ed83f6b638a06135aeff64e0d5383428dda5fa944408c9fe18942f3cd5&token=299008056&lang=zh_CN#rd)

[Flink1.13.0 运行架构介绍——不入坑血亏！](https://mp.weixin.qq.com/s?__biz=Mzg5NDY3NzIwMA==&mid=2247497197&idx=1&sn=1b9b134c1889ce7bf4d4369245136f44&chksm=c0194c52f76ec544c9297a6359e7124cc7b091918405b8284829a63dca576457c11895fc63e5&token=299008056&lang=zh_CN#rd)

[Flink UDAF使用教程！！！](https://mp.weixin.qq.com/s?__biz=Mzg5NDY3NzIwMA==&mid=2247497196&idx=1&sn=0a715919759cf4ebbf82d78e48fb43a0&chksm=c0194c53f76ec545ea0f1c1cac0d8cb3be6d51dc4f8f873311f7404073b37f2a1540193473b5&token=299008056&lang=zh_CN#rd)

[Flink CEP 基本概念及使用规则](https://mp.weixin.qq.com/s?__biz=Mzg5NDY3NzIwMA==&mid=2247497195&idx=1&sn=3afa899382469e9675b93d5e8eea0960&chksm=c0194c54f76ec5420d8b5eab5ef6a27405996c97a4c3bb622dc6f8833012e7a3c7b3ea8fa6dc&token=299008056&lang=zh_CN#rd)

[Flink CEP SQL 使用规则及案例介绍](https://mp.weixin.qq.com/s?__biz=Mzg5NDY3NzIwMA==&mid=2247497195&idx=2&sn=6d5d381ba521cff2a6208a7513ae84fe&chksm=c0194c54f76ec542b8ff25a5c06b62e92ce93e8568d5a48c1cce5c5bdb894a627532be4b62b3&token=299008056&lang=zh_CN#rdv)

[Flink CEP SQL 使用规则及案例介绍](https://mp.weixin.qq.com/s?__biz=Mzg5NDY3NzIwMA==&mid=2247497195&idx=2&sn=6d5d381ba521cff2a6208a7513ae84fe&chksm=c0194c54f76ec542b8ff25a5c06b62e92ce93e8568d5a48c1cce5c5bdb894a627532be4b62b3&token=299008056&lang=zh_CN#rd)

[深入解读 Flink SQL 1.13功能点，不入坑血亏！](https://mp.weixin.qq.com/s?__biz=Mzg5NDY3NzIwMA==&mid=2247497193&idx=1&sn=69819b65b150518dec74494cdfdd0063&chksm=c0194c56f76ec540deb6020e6c340fcb89c5f331f6d87f9e3b6127c15fbf16274c2e3c535ce7&token=299008056&lang=zh_CN#rd)

[原来全链路时延，Flink官方是这样测试的！](https://mp.weixin.qq.com/s?__biz=Mzg5NDY3NzIwMA==&mid=2247497188&idx=1&sn=255a13441f54b727cc60462fba8427af&chksm=c0194c5bf76ec54db5233326c87e4dead48e381578bac27e0bc13ae6453bc541e98c388f4fe1&token=299008056&lang=zh_CN#rd)




## 大数据面试

相信学习 Flink 组件的小伙伴最终的目的都是为了进入大厂，在这里，土哥有着非常丰富的面试经验， 从斩获阿里、蚂蚁金服、字节跳动、拼多多、美团、网易、快手、哔哩哔哩、小红书、得物、顺丰科技、海康威视、Boss 直聘、
流利说、哈罗单车、申通、中通、58 同城、大华、金山云、美的、用友、信也科技、深信服、当贝网络、迅策科技、炎魂网络 等 27 家 offer。

目前在互联网一线大厂工作~ 想了解更多，请点击下列链接查看详情：

[土哥这半年的悲惨人生，经历过被鸽 offer,最终触底反弹~](https://mp.weixin.qq.com/s?__biz=Mzg5NDY3NzIwMA==&mid=2247511408&idx=1&sn=beb292ab97ada3ee486511bfe503117d&chksm=c01914cff76e9dd90fd81857805a57aadcf4fa0a3ce731e5939d8651ed9bac561dba6bb7e03a&scene=21#wechat_redirect)

[土哥社招参加 28 场面试，100% 通过率，拿到全部 offer！](https://mp.weixin.qq.com/s?__biz=Mzg5NDY3NzIwMA==&mid=2247511408&idx=1&sn=beb292ab97ada3ee486511bfe503117d&chksm=c01914cff76e9dd90fd81857805a57aadcf4fa0a3ce731e5939d8651ed9bac561dba6bb7e03a&scene=21#wechat_redirect)

相信大家都听过一句话，一个人可以走的更快，但一群人才能走的更远，所以，为了将自己面试的经验和经历传授给更多人，帮助更多的小伙伴能入职心仪的大厂，所以在空闲时间就辅导求职大数据岗位的小伙伴就业，但无心插柳却柳成荫。

目前已经辅导上百位小伙伴成功拿到 offer，并且多位小伙伴入职互联网大厂，这也成为了土哥的人脉，帮助后续更多的小伙伴进行内推，并少走弯路。详情请看各小伙伴入职大厂信息：

## 辅导内容

如果你是一个人孤军奋战，苦于无人指导、复习无从下手，或者不擅长写简历，手上只有拿不出手的毫无难点亮点的项目经历...
那么我的第一建议是多和身边的大佬沟通，哪怕是付费咨询，只要你能从他身上学到经验，那就是值得的。如果身边没有这样的人，那么我就毛遂自荐一下哈，毕竟，茫茫网络你能看到这篇文章何尝不是一种命运安排。（网络一线牵，珍惜这段缘）
对简历、面试有任何问题都欢迎随时找我，当然是有偿的哈~

毕竟，免费的东西不仅你不会珍惜，你也不相信我真的会用心去帮助你，而我也不愿意舍弃陪老婆的时间去和你1vs1沟通...

有修改简历、面试辅导、项目辅导的想法，欢迎添加土哥微信，备注面试辅导，当然，没有意向的，也欢迎添加土哥微信，毕竟说不定我在朋友圈分享的某一个内容，就是你所感兴趣的呢

<img src="https://files.mdnice.com/user/19005/b13cfefa-1229-455b-a1c1-f2a3c6207ce8.png" alt="img" style="zoom:80%;" />

## 大数据面试系列文章

[58同城大数据开发社招面经(附答案)](https://mp.weixin.qq.com/s?__biz=Mzg5NDY3NzIwMA==&mid=2247506132&idx=1&sn=5e55dffa4ed8dcb137126390fe9283cb&chksm=c019616bf76ee87dfa8a99ac708007d70666822f50edf27a410dbeda1f3f02a016abf9a05c51&token=918623161&lang=zh_CN#rd)

[腾讯微信部门大数据开发面试题-附答案](https://mp.weixin.qq.com/s?__biz=Mzg5NDY3NzIwMA==&mid=2247502003&idx=1&sn=8e5b8dc8af51ded709bc8834a9ccb166&chksm=c019710cf76ef81a4fb5afd6d61f432492c7ba85e122ca16531d5180b5ffaaa87a40aa7725b0&token=918623161&lang=zh_CN#rd)

[58同城大数据开发社招二面|三面附答案](https://mp.weixin.qq.com/s?__biz=Mzg5NDY3NzIwMA==&mid=2247506296&idx=1&sn=1c70af5345a81ae4fb7d927b6b1ba57e&chksm=c01960c7f76ee9d191927f0378213e18f212e6d58f67b19b8e030e70cb3ddd2e92ad06349e83&token=918623161&lang=zh_CN#rd)

[这些都不会，还想进美团？](https://mp.weixin.qq.com/s?__biz=Mzg5NDY3NzIwMA==&mid=2247507174&idx=1&sn=fedb53e3cc716e33856dcdbbd923544b&chksm=c0196559f76eec4f71ca3aa22301d77e1208f0c2f1ae828e3930dd83fbf75325b3d871b24475&token=918623161&lang=zh_CN#rd)

[小姐姐拿哔站搞事情了，“硬怼”面试官！！](https://mp.weixin.qq.com/s?__biz=Mzg5NDY3NzIwMA==&mid=2247507243&idx=1&sn=d0fc996e973aaf32f3df9b2917fd353d&chksm=c0196494f76eed82c012ec02c8db71b800b29967370867e624a703d4a33db456b838227839db&token=918623161&lang=zh_CN#rd)

[小红书真是将压箱底本事都拿出来了，但这又能奈我何！](https://mp.weixin.qq.com/s?__biz=Mzg5NDY3NzIwMA==&mid=2247507460&idx=1&sn=e7400f6cce9c6237ce4892eb3045558a&chksm=c01967bbf76eeeadea4dc5513383ddea2fb31aa584185b2f3eb66638b1a3db68245435177e0b&token=918623161&lang=zh_CN#rd)

[字节跳动大数据架构面经（超详细答案总结）](https://mp.weixin.qq.com/s?__biz=Mzg5NDY3NzIwMA==&mid=2247508412&idx=1&sn=b2f5f3b54b9bf6fe6c5375db6f05901a&chksm=c0191803f76e9115313ee10e61a10096587a2a930d74851f243c1a6c64f754faf459f9cb4848&token=918623161&lang=zh_CN#rd)


[字节跳动大数据架构2面面经（吐血答案总结，超详细）](https://mp.weixin.qq.com/s?__biz=Mzg5NDY3NzIwMA==&mid=2247508685&idx=1&sn=5a882fc2dbd443116b2d45f32ea17484&chksm=c0191b72f76e9264420639756257ba707e8a616cd56b92e07bd4fe2665c027e3fbbc74725c6a&token=918623161&lang=zh_CN#rd)

[拼多多大数据开发1面知识点总结（配视频~超详细）](https://mp.weixin.qq.com/s?__biz=Mzg5NDY3NzIwMA==&mid=2247510180&idx=1&sn=0d7b95a34fe01c1fe58b836e8576f38c&chksm=c019111bf76e980d0b825f351691714d28f8c2af0a4a8fdc54cb8e7225377d838d8ae48879ad&token=918623161&lang=zh_CN#rd)








