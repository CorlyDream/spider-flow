
firefox 限制 cookie 必须在同域名下才能设置，要想解除限制，需要解压 omni.ja 文件，根据关键字：InvalidCookieDomainError 删除相关代码。

可以将 omni.ja 文件视为一个归档文件，类似于 ZIP 文件。要查看或修改其中的内容，你可以解压缩它：

1. 复制备份 omni.ja 文件并重命名为 omni.ja.bak。
2. 使用解压工具（如 unzip）解压文件：
```bash
mkdir omni_modify && cd omni_modify
mv ../omni.ja omni.ja
unzip omni.ja
```
3. 修改所需的文件后（修改内容搜索关键字：InvalidCookieDomainError），再次压缩为 omni.ja：
```bash
zip -qr0XD ../omni.ja *
```

修改内容参考：https://gist.github.com/nddipiazza/1c8cc5ec8dd804f735f772c038483401