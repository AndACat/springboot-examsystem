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
        '    </ul>\n ' +
        '    <div class="form-inline ml-3">\n' +
        '      <div class="input-group input-group-sm">\n' +
        '        <input class="form-control form-control-navbar" id="search" type="search">\n' +
        '        <div class="input-group-append">\n' +
        '          <button class="btn btn-navbar" id="search_button" type="submit">\n' +
        '            <i class="fa fa-search"></i>\n' +
        '          </button>\n' +
        '        </div>\n' +
        '      </div>\n' +
        '    </div>' +
        '    <!-- Right navbar links -->\n' +
        '    <ul class="navbar-nav ml-auto">\n' +
        '      <!-- Messages Dropdown Menu -->\n' +
        '      <li class="nav-item dropdown">\n' +
        '        <a class="nav-link" data-toggle="dropdown" href="#">\n' +
        '          <i class="fa fa-comments"></i>\n' +
        '          <span class="badge badge-danger navbar-badge">3</span>\n' +
        '        </a>\n' +
        '        <div class="dropdown-menu dropdown-menu-lg dropdown-menu-right">\n' +
        '          <a href="#" class="dropdown-item">\n' +
        '            <!-- Message Start -->\n' +
        '            <div class="media">\n' +
        '              <img src="../../dist/img/user1-128x128.jpg" alt="User Avatar" class="img-size-50 mr-3 img-circle">\n' +
        '              <div class="media-body">\n' +
        '                <h3 class="dropdown-item-title">\n' +
        '                  Brad Diesel\n' +
        '                  <span class="float-right text-sm text-danger"><i class="fas fa-star"></i></span>\n' +
        '                </h3>\n' +
        '                <p class="text-sm">Call me whenever you can...</p>\n' +
        '                <p class="text-sm text-muted"><i class="far fa-clock mr-1"></i> 4 Hours Ago</p>\n' +
        '              </div>\n' +
        '            </div>\n' +
        '            <!-- Message End -->\n' +
        '          </a>\n' +
        '\n' +
        '\n' +
        '          <div class="dropdown-divider"></div>\n' +
        '          <a href="#" class="dropdown-item dropdown-footer">See All Messages</a>\n' +
        '        </div>\n' +
        '      </li>\n' +
        '      <!-- Notifications Dropdown Menu -->\n' +
        '      <li class="nav-item dropdown">\n' +
        '        <a class="nav-link" data-toggle="dropdown" href="#">\n' +
        '          <i class="fa fa-bell"></i>\n' +
        '          <span class="badge badge-warning navbar-badge">15</span>\n' +
        '        </a>\n' +
        '        <div class="dropdown-menu dropdown-menu-lg dropdown-menu-right">\n' +
        '          <span class="dropdown-item dropdown-header">15 Notifications</span>\n' +
        '\n' +
        '          <div class="dropdown-divider"></div>\n' +
        '          <a href="#" class="dropdown-item">\n' +
        '            <i class="fa fa-envelope mr-2"></i> 4 new messages\n' +
        '            <span class="float-right text-muted text-sm">3 mins</span>\n' +
        '          </a>\n' +
        '          <div class="dropdown-divider"></div>\n' +
        '          <a href="#" class="dropdown-item dropdown-footer">See All Notifications</a>\n' +
        '        </div>\n' +
        '      </li>\n' +
        '      <li class="nav-item dropdown">\n' +
        '        <a class="nav-link" data-toggle="dropdown" href="#">\n' +
        '          <i class="fa fa-bell"></i>\n' +
        '        </a>\n' +
        '        <div class="dropdown-menu dropdown-menu-right" style="min-width: 7rem;">\n' +
        '          <a href="/goToPage/admin/myInfo" class="dropdown-item">\n' +
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