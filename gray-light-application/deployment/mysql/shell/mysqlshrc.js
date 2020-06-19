/**
 * Mysql Shell的启动脚本，内置一些快速搭建集群的操作
 */

var cluster = shell.createExtensionObject()
var options = {
  seedsCount: 3,
  headlessService: "mysql-local-service",
  podName: "mysql-server",
  groupSeeds: [],
  cluster: null,
  defaultMaster: "mysql-server-0.mysql-local-service",
  clusterName: "yan",
  recoveryMethod: "clone",
  port: 3306,
  localPort: 33061,
  password: "110120",
  admin: "xyparacrim"
}

function address(ip, port) {
  return ip + ":" + port
}

function localAddress(ip) {
  return address(ip, options.localPort)
}

function classAddress(ip) {
  return options.admin + "@" + ip + ":" + options.port
}

function init() {
  if (options.cluster == null) {
    for (var i = 0; i < options.seedsCount; i++) {
      options.groupSeeds.push(options.podName + "-" + i + "." + options.headlessService)
    }

    try {
      options.cluster = dba.getCluster(options.clusterName)
    } catch (e) {
    }

    if (options.cluster == null) {
      options.cluster = dba.createCluster(options.clusterName, {
        groupSeeds: options.groupSeeds.map(localAddress).join(","),
        localAddress: address(options.defaultMaster, options.localPort)
      })
      options.cluster.setupAdminAccount(options.admin)
    }
  }

  return options
}

function addInstance(statefulNumber) {
  options.cluster.addInstance(
    address(options.groupSeeds[statefulNumber], options.port),
    //classAddress(options.groupSeeds[statefulNumber]),
    {
      groupSeeds: options.groupSeeds.map(localAddress).join(","),
      localAddress: localAddress(options.groupSeeds[statefulNumber]),
      recoveryMethod: options.recoveryMethod,
      password: options.password
    }
  )
}


shell.addExtensionObjectMember(cluster, "cluster", function () {
  return options.cluster
})

shell.addExtensionObjectMember(cluster, "init", init, {
  brief: "初始化集群",
  details: ["创建名为yan的集群"]
})

shell.addExtensionObjectMember(cluster, "options", function () {
  return options
})

shell.addExtensionObjectMember(cluster, "addInstance", addInstance, {
  brief: "添加一个实例进入集群",
  details: ["会重启实例，"],
  parameters: [
    {
      name: "statefulNumber",
      type: "integer",
      brief: "statefulset的个数"
    }
  ]
})

shell.addExtensionObjectMember(cluster, "connect", function (statefulNumber) {
  shell.connect({user: options.admin, host: options.groupSeeds[statefulNumber], port: options.port})
}, {
  parameters: [
    {
      name: "statefulNumber",
      type: "integer",
      brief: "statefulset的个数"
    }
  ]
})

shell.addExtensionObjectMember(cluster, "connectByRoot", function (statefulNumber) {
  shell.connect({user: "root", host: options.groupSeeds[statefulNumber], port: options.port})
}, {
  parameters: [
    {
      name: "statefulNumber",
      type: "integer",
      brief: "statefulset的个数"
    }
  ]
})

shell.registerGlobal("yan", cluster)