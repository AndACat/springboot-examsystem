;(function () {
    const template = '<aside class="main-sidebar sidebar-dark-primary elevation-4">' +
        '    <!-- Brand Logo -->' +
        '    <a href="/home" class="brand-link">' +
        '      <img src="../../dist/img/AdminLTELogo.png"' +
        '           alt="ExamSystem Logo"' +
        '           class="brand-image img-circle elevation-3"' +
        '           style="opacity: .8">' +
        '      <span class="brand-text font-weight-light">在线考试系统</span>' +
        '    </a>' +
        '    <!-- Sidebar -->' +
        '    <div class="sidebar">' +
        '      <!-- Sidebar Menu -->' +
        '      <nav class="mt-2">' +
        '        <ul class="nav nav-pills nav-sidebar flex-column nav-child-indent" data-widget="treeview" role="menu" data-accordion="false">' +
        '          <!-- Add icons to the links using the .nav-icon class' +
        '               with font-awesome or any other icon font library -->' +
        '          <li class="nav-item has-treeview">' +
        '            <a href="#" class="nav-link">' +
        '              <i class="nav-icon fa fa-list"></i>' +
        '              <p>出卷<i class="right fa fa-angle-left"></i></p>' +
        '            </a>' +
        '            <ul class="nav nav-treeview">' +
        '              <li class="nav-item">' +
        '                <a href="/teacher/inheritanceAlgorithmPaper.html" class="nav-link">' +
        '                  <i class="fa fa-circle nav-icon text-info"></i><!--fa fa-circle nav-icon-->' +
        '                  <p>智能算法出卷</p>' +
        '                </a>' +
        '              </li>' +
        '            </ul>' +
        '            <ul class="nav nav-treeview">' +
        '              <li class="nav-item">' +
        '                <a href="/teacher/randomPaper.html" class="nav-link">' +
        '                  <i class="fa fa-circle nav-icon text-info"></i>' +
        '                  <p>随机出卷</p>' +
        '                </a>' +
        '              </li>' +
        '            </ul>' +
        '            <ul class="nav nav-treeview">' +
        '              <li class="nav-item">' +
        '                <a href="/teacher/fixation.html" class="nav-link">' +
        '                  <i class="fa fa-circle nav-icon text-info"></i>' +
        '                  <p>固定出卷</p>' +
        '                </a>' +
        '              </li>' +
        '            </ul>' +
        '          </li>' +
        '          <li class="nav-item has-treeview">' +
        '            <a href="#" class="nav-link">' +
        '              <i class="nav-icon fa fa-align-justify"></i>' +
        '              <p>试题管理<i class="right fa fa-angle-left"></i></p>' +
        '            </a>' +
        '            <ul class="nav nav-treeview">' +
        '              <li class="nav-item">' +
        '                <a href="/teacher/singlechoice.html" class="nav-link">' +
        '                  <i class="fa fa-circle nav-icon text-warning"></i>' +
        '                  <p>单选题</p>' +
        '                </a>' +
        '              </li>' +
        '            </ul>' +
        '            <ul class="nav nav-treeview">' +
        '              <li class="nav-item">' +
        '                <a href="/teacher/multiplechoice.html" class="nav-link">' +
        '                  <i class="fa fa-circle nav-icon text-warning"></i>' +
        '                  <p>多选题</p>' +
        '                </a>' +
        '              </li>' +
        '            </ul>' +
        '            <ul class="nav nav-treeview">' +
        '              <li class="nav-item">' +
        '                <a href="/teacher/judge.html" class="nav-link">' +
        '                  <i class="fa fa-circle nav-icon text-warning"></i>' +
        '                  <p>判断题</p>' +
        '                </a>' +
        '              </li>' +
        '            </ul>' +
        '            <ul class="nav nav-treeview">' +
        '              <li class="nav-item">' +
        '                <a href="/teacher/fill.html" class="nav-link">' +
        '                  <i class="fa fa-circle nav-icon text-warning"></i>' +
        '                  <p>填空题</p>' +
        '                </a>' +
        '              </li>' +
        '            </ul>' +
        '            <ul class="nav nav-treeview">' +
        '              <li class="nav-item">' +
        '                <a href="/teacher/short.html" class="nav-link">' +
        '                  <i class="fa fa-circle nav-icon text-warning"></i>' +
        '                  <p>简答题</p>' +
        '                </a>' +
        '              </li>' +
        '            </ul>' +
        '            <ul class="nav nav-treeview">' +
        '              <li class="nav-item">' +
        '                <a href="/teacher/program.html" class="nav-link">' +
        '                  <i class="fa fa-circle nav-icon text-warning"></i>' +
        '                  <p>编程题</p>' +
        '                </a>' +
        '              </li>' +
        '            </ul>' +
        '          </li>' +
        // '          <li class="nav-item">' +
        // '            <a href="/teacher/zb.html" class="nav-link">' +
        // '              <i class="nav-icon fa fa-camera"></i>' +
        // '              <p>作弊查询</p>' +
        // '            </a>' +
        // '          </li>' +
        '          <li class="nav-item">' +
        '            <a href="/teacher/course.html" class="nav-link">' +
        '              <i class="nav-icon fa fa-flag"></i>' +
        '              <p>课程管理</p>' +
        '            </a>' +
        '          </li>' +
        '          <li class="nav-item">' +
        '            <a href="/teacher/marking.html" class="nav-link">' +
        '              <i class="nav-icon fa fa-flag"></i>' +
        '              <p>阅卷</p>' +
        '            </a>' +
        '          </li>' +
        '          <li class="nav-item">' +
        '            <a href="/teacher/studentscore.html" class="nav-link">' +
        '              <i class="nav-icon fa fa-search"></i>' +
        '              <p>成绩查询</p>' +
        '            </a>' +
        '          </li>' +
        '          <li class="nav-item">' +
        '            <a href="/teacher/paperstrategy.html" class="nav-link">' +
        '              <i class="nav-icon fa fa-tags"></i>' +
        '              <p>试卷组成策略管理</p>' +
        '            </a>' +
        '          </li>' +
        '          <li class="nav-item">' +
        '            <a href="/teacher/knowledgeList.html" class="nav-link">' +
        '              <i class="nav-icon fa fa-th"></i>' +
        '              <p>知识点管理</p>' +
        '            </a>' +
        '          </li>' +
        '          <li class="nav-item">' +
        '            <a href="/teacher/paper.html" class="nav-link">' +
        '              <i class="nav-icon fa fa-folder-open"></i>' +
        '              <p>试卷管理</p>' +
        '            </a>' +
        '          </li>' +
        '        </ul>' +
        '      </nav>' +
        '      <!-- /.sidebar-menu -->' +
        '    </div>' +
        '    <!-- /.sidebar -->' +
        '  </aside>';
    window.app_aside={
        template
    }
})()