;(function () {
    const template = '<!-- Navbar -->\n' +
        '  <nav class="main-header navbar navbar-expand navbar-white navbar-light">\n' +
        '    <!-- Left navbar links -->\n' +
        '    <ul class="navbar-nav">\n' +
        '      <li class="nav-item">\n' +
        '        <a class="nav-link" data-widget="pushmenu" href="#"><i class="fa fa-bars"></i></a>\n' +
        '      </li>\n' +
        '      <li class="nav-item d-none d-sm-inline-block">\n' +
        '        <a href="/home" class="nav-link">Home</a>\n' +
        '      </li>\n' +
        '    </ul>\n' +
        '<!-- SEARCH FORM -->' +
        '    <div class="form-inline ml-5">' +
        '       <div class="input-group input-group-sm">' +
        '           <input id="search1" class="form-control form-control-navbar" type="search" placeholder="" aria-label="Search">&nbsp;' +
        '           <input id="search2" class="form-control form-control-navbar" type="search" placeholder="" aria-label="Search">' +
        '           <div class="input-group-append">' +
        '               <button id="search_button" class="btn btn-navbar"><i class="fa fa-search"></i></button>' +
        '           </div>'+
        '       </div>'+
        '    </div>' +
        '    <ul class="navbar-nav ml-auto">\n' +
        '      <li class="nav-item dropdown">\n' +
        '        <a class="nav-link" data-toggle="dropdown" href="#">\n' +
        '          <i class="fa fa-bell"></i>\n' +
        '        </a>\n' +
        '        <div class="dropdown-menu dropdown-menu-right" style="min-width: 7rem;">\n' +
        '          <a href="/teacher/myInfo.html" class="dropdown-item">\n' +
        '            <i class="fas fa-info mr-2"></i>\n' +
        '            <span class="float-right text-muted text-sm">个人信息</span>\n' +
        '          </a>\n' +
        '          <div class="dropdown-divider"></div>\n' +
        '          <a href="/quit" class="dropdown-item">\n' +
        '            <i class="fa fa-power-off mr-2"></i>\n' +
        '            <span class="float-right text-muted text-sm">退出登录</span>\n' +
        '          </a>\n' +
        '        </div>\n' +
        '      </li>\n' +
        '    </ul>\n' +
        '  </nav>\n' +
        '  <!-- /.navbar -->';

    window.app_navbar = {
        template
    }
})()